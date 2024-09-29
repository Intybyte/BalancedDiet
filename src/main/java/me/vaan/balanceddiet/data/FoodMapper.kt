package me.vaan.balanceddiet.data

import me.vaan.balanceddiet.BalancedDiet
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object FoodMapper {
    private val mapper = ConcurrentHashMap<FoodTypes, EnumSet<Material>>()

    fun map(food: Material) : FoodTypes? {
        for (entry in mapper) {
            if (entry.value.contains(food)) return entry.key
        }

        return null
    }

    fun load(file: FileConfiguration) {
        for (foodType in file.getKeys(false)) {
            val list = file.getStringList(foodType).map { Material.valueOf(it) }
            val set = EnumSet.copyOf(list)
            mapper[FoodTypes.valueOf(foodType.uppercase())] = set
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
                    Bukkit.getLogger().warning("Entry $food is not edible in entry ${entry.key}")
                }
            }
        }
    }

    private fun checkForgottenEdibles() {
        val foods = Material.entries.filter { it.isEdible }
        for (food in foods) {
            val type = this.map(food)
            type ?: BalancedDiet.debug("The food $food isn't mapped to anything, you might want to change that")
        }
    }
}