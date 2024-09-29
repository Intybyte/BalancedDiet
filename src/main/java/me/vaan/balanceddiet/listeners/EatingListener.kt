package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.data.DietManager
import me.vaan.balanceddiet.data.FoodEffects
import me.vaan.balanceddiet.data.FoodMapper
import me.vaan.balanceddiet.extension.applyFoodEffect
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.components.FoodComponent

object EatingListener : Listener {
    @EventHandler
    fun onPlayerEat(event: PlayerItemConsumeEvent) {
        val item = event.item
        val food = item.type
        if (!food.isEdible) return

        val foodType = FoodMapper.map(food)
        foodType ?: return

        val record = DietManager[event.player.name]

        val foodComponent = item.itemMeta.food
        val value = record[foodType]
        applyEffect(value, event.player, foodComponent)

        record.addData(foodType, foodComponent.nutrition)
    }

    private fun applyEffect(value: Int, player: Player, foodComponent: FoodComponent) {
        val effect = FoodEffects[value] ?: return
        player.applyFoodEffect(foodComponent, effect)
    }
}
