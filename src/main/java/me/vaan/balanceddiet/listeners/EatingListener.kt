package me.vaan.balanceddiet.listeners

import me.vaan.balanceddiet.BalancedDiet
import me.vaan.balanceddiet.config.ConfigStorage
import me.vaan.balanceddiet.singletons.DietManager
import me.vaan.balanceddiet.singletons.FoodMapper
import me.vaan.balanceddiet.extension.applyFoodEffect
import me.vaan.balanceddiet.extension.getFoodComponent
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

object EatingListener : Listener {
    @EventHandler
    fun onPlayerEat(event: PlayerItemConsumeEvent) {
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE) return

        val item = event.item
        if (!item.type.isEdible) return

        val foodType = FoodMapper.map(item)
        foodType ?: return

        val record = DietManager[player.uniqueId]

        val foodComponent = item.getFoodComponent() ?: return
        val value = record[foodType]
        player.applyFoodEffect(value, foodComponent)

        val changedValue = ConfigStorage.calc(foodComponent.nutrition, foodComponent.saturation)

        BalancedDiet.debug("FoodValue: $value | Adding: $changedValue")
        record.addData(foodType, changedValue)
        BalancedDiet.debug("New Value: " + record[foodType])
    }
}
