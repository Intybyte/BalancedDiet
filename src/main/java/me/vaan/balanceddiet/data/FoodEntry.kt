package me.vaan.balanceddiet.data

import me.vaan.balanceddiet.extension.textContent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class FoodEntry(val material: Material, val display: String?) {

    constructor(stack: ItemStack) : this(stack.type, stack.displayName().textContent())

    operator fun contains(stack: ItemStack) : Boolean {
        val sameMaterial = stack.type == material
        display ?: return sameMaterial

        if (!sameMaterial) return false

        val stackDisplay = stack.displayName().textContent()
        return stackDisplay == display
    }
}
