package me.vaan.balanceddiet.data

object FoodTypes {
    private val registry: HashSet<String> = HashSet()

    fun add(entry: String) {
        registry.add(entry)
    }

    fun clear() {
        registry.clear()
    }

    fun getRegistry(): HashSet<String> {
        return registry
    }
}
