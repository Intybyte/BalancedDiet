package me.vaan.balanceddiet

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import me.vaan.balanceddiet.data.DietManager
import me.vaan.balanceddiet.extension.printDivider
import org.bukkit.entity.Player

@CommandAlias("diet")
object BDCommands : BaseCommand() {
    @Subcommand("profile")
    fun profile(player: Player) {
        player.printDivider()
        player.sendMessage("              DIET STATS              ")
        player.printDivider()
        val record = DietManager[player.name]
        record.print(player)
        player.printDivider()

        val outputFood = "Food: " + player.foodLevel + "     Saturation: "  + player.saturation.toInt()
        player.sendMessage("   $outputFood")
        player.printDivider()
    }

    @Subcommand("save")
    fun save() {
        DietManager.save()
    }
}