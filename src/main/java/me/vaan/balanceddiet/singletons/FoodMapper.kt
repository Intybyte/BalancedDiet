package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.data.FoodEntry
import me.vaan.balanceddiet.data.FoodTypes
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object FoodMapper {
    private val mapper = ConcurrentHashMap<String, HashSet<FoodEntry>>()

    //TODO make a map of nameless items and use both maps
    fun map(food: ItemStack) : String? {
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
            val set = list.map {
                val elements = it.split(";")
                val material = Material.valueOf(elements[0])

                FoodEntry(material,
                    display = if (elements.size == 1) null
                              else elements[2]
                )
            }.toHashSet()


            mapper[lowerFood] = set
        }

        debugAllEntries()
        checkInedible()
        checkForgottenEdibles()
    }

    //region Debug methods
    private fun debugAllEntries() {
        if (!BalancedDiet.debug) return

        for (entry in mapper) {
            BalancedDiet.debug("Food mapper init key: ${entry.key}")

            var s = "["
            for (food in entry.value) {
                s += "$food, "
            }
            s+=']'

            BalancedDiet.debug(s)
        }
    }

    private fun checkInedible() {
        for (entry in mapper) {
            for (food in entry.value) {
                if (!food.material.isEdible && food.material != Material.CAKE) {
                    BalancedDiet.logger!!.warning("Entry $food is not edible in entry ${entry.key}")
                }
            }
        }
    }

    private fun checkForgottenEdibles() {
        val foods = Material.entries.filter { it.isEdible }
        for (food in foods) {
            val type = map(ItemStack(food))
            type ?: BalancedDiet.debug("The food $food isn't mapped to anything, you might want to change that")
        }
    }
    //endregion
}