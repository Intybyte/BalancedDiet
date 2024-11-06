package me.vaan.balanceddiet.menu

import org.bukkit.Material
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class BackItem : PageItem(false) {
    override fun getItemProvider(p0: PagedGui<*>?): ItemProvider {
        val builder = ItemBuilder(Material.ARROW)
            .setDisplayName("Previous Page")
            .addLoreLines(
                if (!this.gui.hasPreviousPage()) "Can't go back"
                else "${gui.currentPage + 1} -> ${gui.currentPage}"
            )

        return builder
    }
}