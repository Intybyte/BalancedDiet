package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.config.ConfigStorage
import me.vaan.balanceddiet.data.FoodEntry
import me.vaan.balanceddiet.data.FoodType
import me.vaan.balanceddiet.data.FoodTypes
import me.vaan.balanceddiet.extension.isDietEdible
import me.vaan.balanceddiet.extension.textContent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

object FoodMapper {
    private val mapper = ConcurrentHashMap<String, HashSet<FoodEntry>>()
    private val defaultMapper = ConcurrentHashMap<Material, String>()

    fun map(food: ItemStack) : String? {
        val display = food.itemMeta.displayName()?.textContent

        val type = food.type
        if (display.isNullOrEmpty()) return defaultMapper[type]
        val foundKey = searchDisplayName(food)
        foundKey ?: return defaultMapper[type]

        return foundKey
    }

    private fun searchDisplayName(food: ItemStack) : String? {
        val searchEntry = FoodEntry(food)
        searchEntry.display ?: return null

        for (entry in mapper) {
            if (searchEntry in entry.value) {
                return entry.key
            }
        }

        return null
    }

    fun load(file: FileConfiguration) {
        for (foodType in file.getKeys(false)) {
            if (foodType == "player") {
                BalancedDiet.logger!!.severe("You can't have a food type called 'player' as it is reserved internally. Skipping config line.")
                continue
            }

            val lowerFood = foodType.lowercase()
            val foodSection = file.getConfigurationSection(foodType)!!

            val list = foodSection.getStringList("foodList")
            val materialItemString = foodSection.getString("material")
            val materialItem: Material
            if (materialItemString == null) {
                BalancedDiet.logger!!.warning("Material entry for $foodType is absent, using BREAD")
                materialItem = Material.BREAD
            } else {
                materialItem = Material.matchMaterial(materialItemString) ?:
                    BalancedDiet.logger!!.warning("$materialItemString can't be resolved into a valid material")
                        .let { Material.BREAD }
            }

            val mm = MiniMessage.miniMessage()
            val displayName = foodSection.getComponent("displayName", mm) ?:
                BalancedDiet.logger!!.warning("$foodType displayName cannot be found, using key")
                    .let { Component.text(foodType) }

            val lore = foodSection.getStringList("lore")
            val loreComponent = lore.map(mm::deserialize)

            val foodTypeEntry = FoodType(materialItem, displayName, loreComponent)
            FoodTypes.add(lowerFood, foodTypeEntry)

            val set = HashSet<FoodEntry>()
            list.forEach {
                val elements = it.split(";")
                val material = Material.matchMaterial(elements[0])

                if (material == null) {
                    BalancedDiet.logger!!.warning("${elements[0]} is an invalid food name")
                    return@forEach
                }

                if (elements.size == 1) {
                    defaultMapper[material] = lowerFood
                } else {
                    set.add(
                        FoodEntry(material, elements[1])
                    )
                }
            }

            mapper[lowerFood] = set
        }

        debugAllEntries()
        checkInedible()
        checkForgottenEdibles()
    }

    //region Debug methods
    private fun debugAllEntries() {
        if (!ConfigStorage.debug) return

        for (entry in mapper) {
            BalancedDiet.debug("Food mapper init key: ${entry.key}")
            val s = "[" + entry.value.joinToString(",") + "]"
            BalancedDiet.debug(s)
        }
    }

    private fun checkInedible() {
        for (entry in mapper) {
            for (food in entry.value) {
                if (!food.material.isDietEdible()) {
                    BalancedDiet.logger!!.warning("Entry $food is not edible in entry ${entry.key}")
                }
            }
        }

        for (entry in defaultMapper) {
            val material = entry.key
            if (!material.isDietEdible()) {
                BalancedDiet.logger!!.warning("Default Entry $material is not edible in entry ${entry.value}")
            }
        }
    }

    private fun checkForgottenEdibles() {
        val foods = Material.values().filter(Material::isDietEdible)
        for (food in foods) {
            defaultMapper[food]
                ?: BalancedDiet.logger!!.warning("The food $food isn't mapped to any default, you might want to change that")
        }
    }
    //endregion
}