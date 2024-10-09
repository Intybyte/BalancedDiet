package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.data.FoodTypes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object FoodMapper {
    private val mapper = ConcurrentHashMap<String, EnumSet<Material>>()

    fun map(food: Material) : String? {
        for (entry in mapper) {
            if (entry.value.contains(food)) return entry.key
        }

        return null
    }

    fun load(file: FileConfiguration) {
        for (foodType in file.getKeys(false)) {
            if (foodType == "player") {
                BalancedDiet.logger!!.severe("You can't have a food type called 'player' as it is reserved internally. Skipping config line.")
                continue
            }

            FoodTypes.add(foodType.lowercase())
            val list = file.getStringList(foodType).map { Material.valueOf(it) }
            val set = EnumSet.copyOf(list)
            mapper[foodType.lowercase()] = set
        }

        debugAllEntries()
        checkInedible()
        checkForgottenEdibles()
    }

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
                if (!food.isEdible && food != Material.CAKE) {
                    BalancedDiet.logger!!.warning("Entry $food is not edible in entry ${entry.key}")
                }
            }
        }
    }

    private fun checkForgottenEdibles() {
        val foods = Material.entries.filter { it.isEdible }
        for (food in foods) {
            val type = map(food)
            type ?: BalancedDiet.debug("The food $food isn't mapped to anything, you might want to change that")
        }
    }
}