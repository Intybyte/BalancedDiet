package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.ConfigStorage
import me.vaan.balanceddiet.data.FoodEntry
import me.vaan.balanceddiet.data.FoodTypes
import me.vaan.balanceddiet.extension.isDietEdible
import me.vaan.balanceddiet.extension.textContent
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

object FoodMapper {
    private val mapper = ConcurrentHashMap<String, HashSet<FoodEntry>>()
    private val defaultMapper = ConcurrentHashMap<Material, String>()

    fun map(food: ItemStack) : String? {
        val display = food.displayName().textContent()

        val type = food.type
        if (display.isEmpty()) return defaultMapper[type]
        val foundKey = searchDisplayName(food)
        foundKey ?: return defaultMapper[type]

        return foundKey
    }

    private fun searchDisplayName(food: ItemStack) : String? {
        for (entry in mapper) {
            val searchEntry = FoodEntry(food)

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
            FoodTypes.add(lowerFood)
            val list = file.getStringList(foodType)
            val set = HashSet<FoodEntry>()
            list.forEach {
                val elements = it.split(";")
                val material = Material.valueOf(elements[0])

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
        val foods = Material.entries.filter { it.isDietEdible() }
        for (food in foods) {
            defaultMapper[food]
                ?: BalancedDiet.logger!!.warning("The food $food isn't mapped to any default, you might want to change that")
        }
    }
    //endregion
}