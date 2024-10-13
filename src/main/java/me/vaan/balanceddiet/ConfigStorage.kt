package me.vaan.balanceddiet

import org.bukkit.configuration.file.FileConfiguration

object ConfigStorage {
    var debug = false

    fun load(config: FileConfiguration) {
        debug = config.getBoolean("debug")
    }
}