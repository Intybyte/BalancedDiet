package me.vaan.balanceddiet.extension

import me.vaan.balanceddiet.data.FoodEffects
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.components.FoodComponent

fun Player.applyFoodEffect(value: Int, foodComponent: FoodComponent) {
    val foodEffect = FoodEffects[value] ?: return

    if (foodEffect.damage > 0.1)
        this.damage(foodEffect.damage / 2.0 )

    val foodToAdd = foodComponent.nutrition * (foodEffect.foodMultiplier - 1.0)
    this.foodLevel += foodToAdd.toInt()

    val saturationToAdd = foodComponent.saturation * (foodEffect.saturationMultiplier - 1.0)
    this.saturation += saturationToAdd.toInt()

    for (effect in foodEffect.potionEffect) {
        this.addPotionEffect(effect)
    }
}

fun Audience.printDivider() {
    this.sendMessage(Component.text("=".repeat(34)))
}