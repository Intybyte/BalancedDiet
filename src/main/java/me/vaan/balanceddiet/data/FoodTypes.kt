package me.vaan.balanceddiet.data

object FoodTypes {
    val registry: HashMap<String, FoodType> = HashMap()

    fun add(key: String, entry: FoodType) {
        registry[key] = entry
    }

    fun clear() {
        registry.clear()
    }
}
