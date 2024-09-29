package me.vaan.balanceddiet.data

import java.util.concurrent.ConcurrentHashMap

object DietManager {
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
}
