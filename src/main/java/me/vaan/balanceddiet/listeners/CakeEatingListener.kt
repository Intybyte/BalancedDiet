package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.data.DietManager
import me.vaan.balanceddiet.data.FoodMapper
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object CakeEatingListener : Listener {
    @EventHandler
    fun cakeEat(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return

        val cake = event.clickedBlock ?: return

        if (cake.type != Material.CAKE) return

        val foodType = FoodMapper.map(Material.CAKE) ?: return
        val player = event.player

        //only add data, so it won't be used to spam effects
        val record = DietManager[player.name]
        record.addData(foodType, 1)
    }
}