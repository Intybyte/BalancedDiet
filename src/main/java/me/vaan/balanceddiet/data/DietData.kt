package me.vaan.balanceddiet.data

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class DietData {
    private val _data = ConcurrentHashMap<FoodTypes, Int>()

    constructor(values: IntArray) {
        val foodTypes = FoodTypes.entries.toTypedArray()

        for (i in values.indices) {
            _data[foodTypes[i]] = values[i]
        }
    }

    constructor() : this( IntArray(FoodTypes.entries.size) { 0 } )

    fun toIntArray() : IntArray {
        return _data.values.toIntArray()
    }

    fun decreaseAll(amount: Int) {
        val foodTypes = FoodTypes.entries.toTypedArray()

        for (t in foodTypes) {
            val stored = _data[t] ?: continue
            _data[t] = max(0, stored - amount)
        }
    }

    fun addData(type: FoodTypes, offset: Int) {
        _data[type] = (_data[type] ?: 0) + offset
    }

    operator fun get(type: FoodTypes) : Int {
        return _data[type]!!
    }

    fun print(p: Audience) {
        _data.forEach { (t, u) ->
            p.sendMessage(Component.text("$t => Consumed $u"))
        }
    }
}
