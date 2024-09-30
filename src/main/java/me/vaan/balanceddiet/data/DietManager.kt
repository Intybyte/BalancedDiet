package me.vaan.balanceddiet.data

import me.vaan.balanceddiet.BalancedDiet
import org.bukkit.Bukkit
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.concurrent.ConcurrentHashMap

object DietManager {
    private const val dbName = "dietDatabase.db"
    private lateinit var connection: Connection
    private val database = ConcurrentHashMap<String, DietData>()

    private fun initRecord(name: String) {
        if(database[name] == null) {
            database[name] = DietData()
        }
    }

    operator fun get(name: String): DietData {
        initRecord(name)
        return database[name]!!
    }

    fun save(async: Boolean) {
        val runnable =  {
            for (entry in database) {
                val statement = connection
                    .prepareStatement("REPLACE INTO diet (player, dietArray) VALUES (?, ?)")
                statement.setString(1, entry.key)

                val intArray = entry.value.toIntArray()
                val stringArray = intArray.joinToString(",")

                statement.setString(2, stringArray)
                statement.execute()
            }
        }

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(BalancedDiet.instance, runnable)
        } else {
            runnable.invoke()
        }
    }

    fun init(plugin: BalancedDiet) {
        val dbFile = File(plugin.dataFolder, dbName)
        if (!dbFile.exists()) dbFile.createNewFile()

        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.path}")
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS diet (" +
                    "player TEXT PRIMARY KEY, " +
                    "dietArray TEXT)"
        )
    }

    fun load() {
        val resultSet: ResultSet = connection.createStatement().executeQuery("SELECT * FROM diet")
        while (resultSet.next()) {
            val player = resultSet.getString("player")
            val stringArray = resultSet.getString("dietArray")
            val dietArray = stringArray.split(",").map { it.toInt() }.toIntArray()
            database[player] = DietData(dietArray)
        }
    }

    fun close() {
        try {
            if (!connection.isClosed)
                connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
