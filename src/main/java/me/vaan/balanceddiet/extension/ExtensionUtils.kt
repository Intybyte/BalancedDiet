package me.vaan.balanceddiet.extension

import me.vaan.balanceddiet.data.FoodEffects
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.components.FoodComponent
import java.lang.reflect.Method

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

fun ItemStack.getFood() : Int {
    return try {
        val foodClass = Class.forName("net.minecraft.world.item.ItemFood")
        val itemType = Class.forName("net.minecraft.world.item.Item")
        val itemField = itemType.getDeclaredField("foodProperties")
        itemField.isAccessible = true // Make the field accessible

        // Get the food properties
        val foodProperties = itemField.get(this)
        val getHungerMethod: Method = foodClass.getDeclaredMethod("getNutrition") // Method to get hunger restoration
        getHungerMethod.isAccessible = true // Make the method accessible

        // Call the method and return the hunger restoration value
        getHungerMethod.invoke(foodProperties) as Int
    } catch (e: Exception) {
        e.printStackTrace() // Handle any exceptions
        0 // Return 0 if something goes wrong
    }
}

fun Audience.printDivider() {
    this.sendMessage(Component.text("=".repeat(34)))
}