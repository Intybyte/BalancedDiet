package me.vaan.balanceddiet.extension

import me.vaan.balanceddiet.singletons.FoodEffects
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import org.bukkit.Material
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Player.applyFoodEffect(value: Int, foodComponent: FoodProperties) {
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

fun ItemStack.getFoodComponent() : FoodProperties? {
    val nmsCopy = CraftItemStack.asNMSCopy(this)
    return nmsCopy.components[DataComponents.FOOD]
}

fun Component.textContent() : String {
    return (this as TextComponent).content()
}

fun Material.isDietEdible() : Boolean {
    return this == Material.CAKE || this.isEdible
}