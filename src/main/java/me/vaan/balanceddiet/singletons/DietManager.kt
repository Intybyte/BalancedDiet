package me.vaan.balanceddiet.singletons

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.data.DietData
import me.vaan.balanceddiet.data.FoodTypes
import org.bukkit.Bukkit
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object DietManager {
    private const val dbName = "dietDatabase.db"
    private lateinit var connection: Connection
    private val database = ConcurrentHashMap<UUID, DietData>()

    private fun initRecord(id: UUID) {
        if(database[id] == null) {
            database[id] = DietData()
        }
    }

    operator fun get(id: UUID): DietData {
        initRecord(id)
        return database[id]!!
    }

    operator fun set(id: UUID, data: DietData) {
        database[id] = data
    }

    fun save(async: Boolean) {
        val runnable =  {
            val registry = FoodTypes.getRegistry()
            val fieldsToChange = registry.joinToString(",")
            val placeholders = "?,".repeat(registry.size + 1).dropLast(1)

            for (entry in database) {
                val player = entry.key
                val foodData = entry.value

                val statement = connection
                    .prepareStatement("REPLACE INTO diet (player, $fieldsToChange) VALUES ($placeholders)")
                statement.setString(1, player.toString())

                var i = 2
                for (food in registry) {
                    statement.setInt(i++, foodData[food])
                }

                statement.execute()
            }
        }

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(BalancedDiet.instance!!, runnable)
        } else {
            runnable.invoke()
        }
    }

    fun init(plugin: BalancedDiet) {
        val dbFile = File(plugin.dataFolder, dbName)
        if (!dbFile.exists()) dbFile.createNewFile()

        connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.path}")
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS diet (player TEXT PRIMARY KEY)"
        )

        val columnsList = getColumns("diet")
        for (entry in FoodTypes.getRegistry()) {
            if (entry !in columnsList) {
                val statement = connection.createStatement()
                val alterTableQuery = "ALTER TABLE diet ADD COLUMN $entry INTEGER"
                statement.executeUpdate(alterTableQuery)
            }
        }
    }

    fun load() {
        val resultSet: ResultSet = connection.createStatement().executeQuery("SELECT * FROM diet")
        while (resultSet.next()) {
            val player = UUID.fromString(resultSet.getString("player"))
            val tempData = DietData()
            for (entry in FoodTypes.getRegistry()) {
                try {
                    val foodEntryValue = resultSet.getInt(entry)
                    tempData[entry] = foodEntryValue
                } catch (e: Exception) {
                    BalancedDiet.logger!!.warning("Error loading database: $entry not found")
                    tempData[entry] = 0
                }
            }

            database[player] = tempData
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

    private fun getColumns(tableName: String): MutableList<String> {
        return getColumns(tableName) { _ -> true }
    }

    private fun getColumns(tableName: String, filter: (ResultSet) -> Boolean) : MutableList<String> {
        val result = LinkedList<String>()

        val statement = connection.createStatement()
        val pragmaQuery = "PRAGMA table_info($tableName);"
        val resultSet = statement.executeQuery(pragmaQuery)

        while (resultSet.next()) {
            if (!filter(resultSet)) {
                continue
            }

            val existingColumn: String = resultSet.getString("name")
            result.add(existingColumn)
        }

        return result
    }
}
