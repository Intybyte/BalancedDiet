package me.vaan.balanceddiet.data

import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max

class DietData {
    private val _data = ConcurrentHashMap<FoodTypes, Int>()
    /*val data: ConcurrentHashMap<FoodTypes, Int>
        get() {
            return _data
        }*/

    constructor(values: IntArray) {
        val foodTypes = FoodTypes.entries.toTypedArray()

        for (i in values.indices) {
            _data[foodTypes[i]] = values[i]
        }
    }

    constructor() : this( IntArray(FoodTypes.entries.size) { 0 } )

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
}
