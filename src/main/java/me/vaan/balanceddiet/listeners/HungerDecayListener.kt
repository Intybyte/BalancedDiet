package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.data.DietManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

object HungerDecayListener : Listener {
    @EventHandler
    fun onFoodLower(event: FoodLevelChangeEvent) {
        if(event.item != null) return
        if(event.entity !is Player) return

        val player = event.entity
        if(event.foodLevel >= player.foodLevel) return

        val record = DietManager[player.name]
        record.decreaseAll(1)
    }
}