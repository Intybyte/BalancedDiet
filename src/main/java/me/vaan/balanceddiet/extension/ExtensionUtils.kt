package me.vaan.balanceddiet.extension

import me.vaan.balanceddiet.data.FoodEffects
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.components.FoodComponent

fun Player.applyFoodEffect(value: Int, foodComponent: FoodComponent) {
    val effect = FoodEffects[value] ?: return

    if (effect.damage > 0.1)
        this.damage(effect.damage / 2.0 )

    val foodToAdd = foodComponent.nutrition * (effect.foodMultiplier - 1.0)
    this.foodLevel += foodToAdd.toInt()

    val saturationToAdd = foodComponent.saturation * (effect.saturationMultiplier - 1.0)
    this.saturation += saturationToAdd.toInt()

    for (effect in effect.potionEffect) {
        this.addPotionEffect(effect)
    }
}