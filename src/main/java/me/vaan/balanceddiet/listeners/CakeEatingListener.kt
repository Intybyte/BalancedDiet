package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.singletons.DietManager
import me.vaan.balanceddiet.singletons.FoodMapper
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object CakeEatingListener : Listener {
    @EventHandler
    fun cakeEat(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return

        val cake = event.clickedBlock ?: return

        if (cake.type != Material.CAKE) return

        val foodType = FoodMapper.map(ItemStack(Material.CAKE)) ?: return
        val player = event.player

        //only add data, so it won't be used to spam effects
        val record = DietManager[player.uniqueId]
        record.addData(foodType, 1 * 4)
    }
}