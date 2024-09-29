package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.data.DietManager
import me.vaan.balanceddiet.data.FoodMapper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

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



        record.addData(foodType, foodComponent.nutrition)
    }
}
