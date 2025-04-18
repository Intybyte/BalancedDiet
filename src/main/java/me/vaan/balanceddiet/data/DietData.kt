package me.vaan.balanceddiet.data

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class DietData {
    private val _data = ConcurrentHashMap<String, Int>()

    init {
        FoodTypes.registry.forEach {
            _data[it.key] = 0
        }
    }

    fun decreaseAll(amount: Int) {
        for (t in FoodTypes.registry) {
            val stored = _data[t.key] ?: continue
            _data[t.key] = max(0, stored - amount)
        }
    }

    fun addData(type: String, offset: Int) {
        _data[type] = (_data[type] ?: 0) + offset
    }

    operator fun get(type: String) : Int {
        return _data[type]!!
    }

    operator fun set(type: String, amount: Int) {
        _data[type] = amount
    }

    fun print(p: Audience) {
        _data.forEach { (t, u) ->
            p.sendMessage(Component.text("$t => Consumed $u"))
        }
    }

    fun <T> map(consumer: (Map.Entry<String, Int>) -> T) : List<T> {
        return _data.map(consumer).toMutableList()
    }
}
