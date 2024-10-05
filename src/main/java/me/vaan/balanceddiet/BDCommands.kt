package me.vaan.balanceddiet

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.vaan.balanceddiet.data.DietData
import me.vaan.balanceddiet.singletons.DietManager
import me.vaan.balanceddiet.singletons.FoodEffects
import me.vaan.balanceddiet.extension.printDivider
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("diet")
@Description("Base command of BalancedDiet plugin")
object BDCommands : BaseCommand() {

    @CommandPermission("balanceddiet.command.profile")
    @Description("Gives info about the player diet")
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

    @CommandPermission("balanceddiet.command.profile.other")
    @Description("Gives info about another player diet")
    @Subcommand("profile")
    fun otherProfile(commandSender: CommandSender, playerName: String) {
        val player = Bukkit.getOfflinePlayer(playerName)

        if (!player.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found!")
            return
        }

        commandSender.printDivider()
        commandSender.sendMessage("              DIET STATS              ")
        commandSender.printDivider()

        val record = DietManager[playerName]
        record.print(commandSender)
        commandSender.printDivider()

        // print food and saturation if player is online too
        val onlinePlayer = player.player
        if (onlinePlayer != null) {
            val outputFood = "Food: " + onlinePlayer.foodLevel + "     Saturation: "  + onlinePlayer.saturation.toInt()
            commandSender.sendMessage("   $outputFood")
            commandSender.printDivider()
        }
    }

    @CommandPermission("balanceddiet.command.effects")
    @Description("Gives info about the positive/negative effects of follow a good/bad diet")
    @Subcommand("effects")
    fun effects(sender: CommandSender) {
        sender.printDivider()
        sender.sendMessage("             FOOD EFFECTS             ")
        sender.printDivider()
        FoodEffects.print(sender)
        sender.printDivider()
    }

    @CommandPermission("balanceddiet.command.save")
    @Description("Saves to the database async")
    @Subcommand("save")
    fun save(sender: CommandSender) {
        DietManager.save(true)
    }

    @CommandPermission("balanceddiet.command.clear")
    @Description("Clears your player diet")
    @Subcommand("clear")
    fun clear(player: Player) {
        DietManager[player.name] = DietData()
        player.sendMessage("Success!")
    }

    @CommandPermission("balanceddiet.command.clear")
    @Description("Clears another player diet")
    @Subcommand("clear")
    fun clearOther(commandSender: CommandSender, playerName: String) {
        val player = Bukkit.getOfflinePlayer(playerName)
        if (!player.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found!")
            return
        }

        DietManager[playerName] = DietData()
        commandSender.sendMessage("Success!")
    }

}