package me.vaan.balanceddiet.menu

import me.vaan.balanceddiet.singletons.DietManager
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Click
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

object Menu {
    private val useless = { click: Click -> click.event.isCancelled = true }
    private val background = SimpleItem(ItemBuilder(Material.BLACK_STAINED_GLASS_PANE), useless)

    private val dietMenuPrefab = PagedGui.items()
        .setStructure(
            "# # # # # # # # #",
            "# x x x x x x x #",
            "# # < # O # > # #"
        )
        .addIngredient('#', background)
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('<', BackItem())
        .addIngredient('>', NextItem())

    fun dietMenu(player: Player): Gui {
        val skull = player.head

        val foodItem = ItemBuilder(skull)
            .setDisplayName("Food Stats")
            .addLoreLines("", "Food: ${player.foodLevel}", "Saturation: ${player.saturation}")

        val record = DietManager[player.uniqueId]
        val items = record.map {
            val builder = ItemBuilder(Material.BREAD) //TODO: make this configurable
                .setDisplayName(it.key)
                .addLoreLines("", "Consumed: ${it.value}")

            SimpleItem(builder, useless)
        }

        val gui = dietMenuPrefab
            .clone()
            .addIngredient('O', foodItem)
            .setContent(items)
            .build()

        return gui
    }

    private val Player.head : ItemStack get() {
        val skull = ItemStack(Material.PLAYER_HEAD)
        val skullMeta = skull.itemMeta as SkullMeta
        skullMeta.setOwningPlayer(player)
        skull.itemMeta = skullMeta
        return skull
    }
}