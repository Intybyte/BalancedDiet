package me.vaan.balanceddiet.data

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class DietData {
    private val _data = ConcurrentHashMap<String, Int>()

    constructor(values: IntArray) {
        val foodTypes = FoodTypes.getRegistry()

        for (i in values.indices) {
            _data[foodTypes[i]] = values[i]
        }
    }

    constructor() : this( IntArray(FoodTypes.getRegistry().size) { 0 } )

    fun toIntArray() : IntArray {
        return _data.values.toIntArray()
    }

    fun decreaseAll(amount: Int) {
        val foodTypes = FoodTypes.getRegistry()

        for (t in foodTypes) {
            val stored = _data[t] ?: continue
            _data[t] = max(0, stored - amount)
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
}
