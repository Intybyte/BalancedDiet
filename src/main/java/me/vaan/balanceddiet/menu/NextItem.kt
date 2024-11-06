package me.vaan.balanceddiet.menu

import org.bukkit.Material
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.PageItem

class NextItem : PageItem(true) {
    override fun getItemProvider(p0: PagedGui<*>?): ItemProvider {
        val builder = ItemBuilder(Material.ARROW)
            .setDisplayName("Previous Page")
            .addLoreLines(
                if (!this.gui.hasPreviousPage()) "No more pages"
                else "${gui.currentPage + 1} -> ${gui.currentPage + 2}"
            )

        return builder
    }
}
