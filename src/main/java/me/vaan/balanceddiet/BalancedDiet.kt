package me.vaan.balanceddiet

import co.aikar.commands.PaperCommandManager
import me.vaan.balanceddiet.singletons.DietManager
import me.vaan.balanceddiet.singletons.FoodEffects
import me.vaan.balanceddiet.singletons.FoodMapper
import me.vaan.balanceddiet.listeners.CakeEatingListener
import me.vaan.balanceddiet.listeners.EatingListener
import me.vaan.balanceddiet.listeners.HungerDecayListener
import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.logging.Logger

class BalancedDiet : JavaPlugin() {
    companion object Static {
        
        var logger: Logger? = null
            private set

        var instance: BalancedDiet? = null
            private set

        var debug: Boolean = false
            private set

        fun debug(s: String) {
            if (debug) {
                logger?.info(s)
            }
        }
    }

    override fun onEnable() {
        instance = this
        Static.logger = this.logger
        initFiles()
        initCommands()
        initListeners()

        FoodMapper.load( getConfiguration("foodTypes.yml") )
        FoodEffects.load( getFile("foodEffects.yml") )
        DietManager.init(this)
        DietManager.load()
    }

    override fun onDisable() {
        DietManager.save(false)
        DietManager.close()
    }

    private fun initCommands() {
        val pcm = PaperCommandManager(this)
        pcm.registerCommand(BDCommands)
    }

    private fun initListeners() {
        server.pluginManager.registerEvents(EatingListener, this)
        server.pluginManager.registerEvents(HungerDecayListener, this)
        server.pluginManager.registerEvents(CakeEatingListener, this)
    }

    private fun initFiles() {
        saveDefaultConfig()
        getFile("foodTypes.yml")
        getFile("foodEffects.yml")

        debug = config.getBoolean("debug")
    }

    fun getFile(path: String) : File {
        val configFile = File(dataFolder, path)
        if (!configFile.exists()) saveResource(path, false)
        return configFile
    }

    fun getConfiguration(path: String) : FileConfiguration {
        val configFile = getFile(path)

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
