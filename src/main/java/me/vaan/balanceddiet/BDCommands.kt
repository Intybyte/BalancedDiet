package me.vaan.balanceddiet

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.vaan.balanceddiet.data.DietData
import me.vaan.balanceddiet.singletons.DietManager
import me.vaan.balanceddiet.singletons.FoodEffects
import me.vaan.balanceddiet.menu.Menu
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.xenondevs.invui.window.Window

@CommandAlias("diet")
@Description("Base command of BalancedDiet plugin")
object BDCommands : BaseCommand() {

    @CommandPermission("balanceddiet.command.profile")
    @Description("Gives info about the player diet")
    @Subcommand("profile")
    fun profile(player: Player) {
        val gui = Menu.dietMenu(player) ?: return

        val window = Window.single()
            .setViewer(player)
            .setTitle("Your Diet Profile")
            .setGui(gui)
            .build()

        window.open()
    }

    @CommandPermission("balanceddiet.command.profile.other")
    @Description("Gives info about another player diet")
    @Subcommand("profile")
    fun otherProfile(player: Player, playerName: String) {
        val subject = Bukkit.getOfflinePlayer(playerName)

        val gui = Menu.dietMenu(subject)
        if (gui == null) {
            player.sendMessage("Player not found!")
            return
        }

        val window = Window.single()
            .setViewer(player)
            .setTitle("$playerName's Diet Profile")
            .setGui(gui)
            .build()

        window.open()
    }

    @CommandPermission("balanceddiet.command.effects")
    @Description("Gives info about the positive/negative effects of follow a good/bad diet")
    @Subcommand("effects")
    fun effects(sender: CommandSender) {
        sender.sendMessage("             FOOD EFFECTS             ")
        FoodEffects.print(sender)
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
        DietManager[player.uniqueId] = DietData()
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

        DietManager[player.uniqueId] = DietData()
        commandSender.sendMessage("Success!")
    }

}