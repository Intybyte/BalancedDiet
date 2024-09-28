package me.vaan.balanceddiet

import me.vaan.balanceddiet.data.FoodMapper
import me.vaan.balanceddiet.listeners.EatingListener
import me.vaan.balanceddiet.listeners.HungerDecayListener
import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class BalancedDiet : JavaPlugin() {
    companion object {
        private var debug: Boolean = false

        fun debug(s: String) {
            if (debug) {
                Bukkit.getLogger().info(s)
            }
        }
    }

    override fun onEnable() {
        initFiles()
        initListeners()

        FoodMapper.load(this)
    }

    private fun initListeners() {
        server.pluginManager.registerEvents(EatingListener, this)
        server.pluginManager.registerEvents(HungerDecayListener, this)
    }

    private fun initFiles() {
        saveDefaultConfig()
        getFile("foodTypes.yml")

        debug = config.getBoolean("debug")
    }

    fun getFile(path: String) : FileConfiguration {
        val configFile = File(dataFolder, path)

        if (!configFile.exists()) saveResource(path, false)

        val config: FileConfiguration = YamlConfiguration()
        try {
            config.load(configFile)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }

        return config
    }
}
