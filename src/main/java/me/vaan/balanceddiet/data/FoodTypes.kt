package me.vaan.balanceddiet.data

object FoodTypes {
    private val registry: MutableList<String> = ArrayList()

    fun add(entry: String) {
        registry.add(entry)
    }

    fun getRegistry(): MutableList<String> {
        return registry
    }
}
