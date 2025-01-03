package me.vaan.balanceddiet

import co.aikar.commands.PaperCommandManager
import me.vaan.balanceddiet.config.ConfigStorage
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
import xyz.xenondevs.inventoryaccess.version.InventoryAccessRevision
import java.io.File
import java.io.IOException
import java.util.logging.Logger

class BalancedDiet : JavaPlugin() {
    companion object Static {
        
        var logger: Logger? = null
            private set

        var instance: BalancedDiet? = null
            private set

        fun debug(s: String) {
            if (ConfigStorage.debug) {
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

        InventoryAccessRevision.R1
        InventoryAccessRevision.REQUIRED_REVISION
        println(InventoryAccessRevision.REQUIRED_REVISION)

        FoodMapper.load( getConfiguration("foodTypes.yml") )
        FoodEffects.load( getFile("foodEffects.yml") )
        DietManager.init(this)
        DietManager.load()

        val min15 = 15 * 60 * 20L  //save every 15 min
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            DietManager.save(true)
        }, min15, min15)
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
        ConfigStorage.load(config)
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
