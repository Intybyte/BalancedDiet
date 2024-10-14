package me.vaan.balanceddiet.config

import org.bukkit.configuration.file.FileConfiguration

object ConfigStorage {
    var debug = false
    private lateinit var calcData: CalculationData

    fun load(config: FileConfiguration) {
        debug = config.getBoolean("debug")
        calcData = CalculationData(config)
    }

    fun calc(nutrition: Int, saturation: Float) : Int {
        var finalValue = 0.0

        finalValue += if (calcData.nutrition) nutrition else 0
        finalValue += if (calcData.saturation) saturation else 0f
        finalValue += calcData.baseOffset

        finalValue *= calcData.multiplier

        finalValue += calcData.afterOffset

        return finalValue.toInt()
    }
}