package me.vaan.balanceddiet.config

import org.bukkit.configuration.file.FileConfiguration

data class CalculationData(
    val nutrition: Boolean,
    val saturation: Boolean,
    val baseOffset: Int,
    val multiplier: Double,
    val afterOffset: Int
) {
    constructor(config: FileConfiguration) : this(
        config.getBoolean("calculation.use-nutrition", true),
        config.getBoolean("calculation.use-saturation", true),
        config.getInt("calculation.base-offset", 0),
        config.getDouble("calculation.multiplier", 2.0),
        config.getInt("calculation.after-offset", 0)
    )
}
