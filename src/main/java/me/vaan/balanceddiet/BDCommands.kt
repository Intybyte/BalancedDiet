package me.vaan.balanceddiet

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import me.vaan.balanceddiet.data.DietManager
import org.bukkit.entity.Player

@CommandAlias("diet")
object BDCommands : BaseCommand() {
    @Subcommand("profile")
    fun profile(player: Player) {
        player.sendMessage("==================================")
        player.sendMessage("            DIET STATS            ")
        player.sendMessage("==================================")
        val record = DietManager[player.name]
        record.print(player)
        player.sendMessage("==================================")
    }
}