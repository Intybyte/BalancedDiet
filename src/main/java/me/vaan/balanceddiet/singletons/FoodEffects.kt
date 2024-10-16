package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.config.ConfigStorage
import me.vaan.balanceddiet.data.DietEffect
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object FoodEffects {
    private val mapper = ConcurrentHashMap<IntRange, DietEffect>()

    operator fun get(key: Int): DietEffect? {
        for (entry in mapper) {
            if (key in entry.key) {
                return entry.value
            }
        }

        return null
    }

    fun load(yamlFile: File) {
        val yaml = Yaml()
        val data: Map<String, Map<String, Any>> = yaml.load(yamlFile.inputStream())

        for ((rangeKey, values) in data) {
            // Parse the range (e.g., "0-5" to IntRange)
            val (startString, endString) = rangeKey.split("-")
            val start = startString.toInt()
            val end = if(endString == "ANY") Int.MAX_VALUE else endString.toInt()

            val range = IntRange(start, end)

            // Extract potion effects
            val effectsList = (values["effects"] as? List<List<String>>)?.mapNotNull { effect ->
                val effectType = PotionEffectType.getByName(effect[0].uppercase())
                val level = effect[1].toInt() - 1
                val duration = effect[2].toInt() * 20

                if (effectType != null) {
                    PotionEffect(effectType, duration, level)
                } else {
                    null
                }
            } ?: listOf()

            // Extract optional fields with defaults
            val saturationMultiplier = values["saturation-multiplier"] as? Double ?: 1.0
            val foodMultiplier = values["food-multiplier"] as? Double ?: 1.0
            val damage = values["damage"] as? Double ?: 0.0

            // Create DietEffect object
            val dietEffect = DietEffect(
                potionEffect = effectsList,
                saturationMultiplier = saturationMultiplier,
                foodMultiplier = foodMultiplier,
                damage = damage
            )

            mapper[range] = dietEffect
        }

        debugAllEntries()
    }

    private fun debugAllEntries() {
        if (!ConfigStorage.debug) return

        for (entry in mapper) {
            BalancedDiet.debug("Key: ${entry.key} | Value: ${entry.value}")
        }
    }

    fun print(audience: Audience) {
        for (entry in mapper) {
            val message = Component.text("Key: ${entry.key} | Value: ${entry.value}")
            audience.sendMessage(message)
        }
    }
}