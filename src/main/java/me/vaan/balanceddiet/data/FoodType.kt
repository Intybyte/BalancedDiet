package me.vaan.balanceddiet.data

import me.vaan.balanceddiet.extension.italicLess
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.addLoreLines
import xyz.xenondevs.invui.item.builder.setDisplayName

data class FoodType(
    val displayItem: ItemBuilder
) {
    constructor(material: Material, name: Component, lore: List<Component>) : this(
            ItemBuilder(material)
                .setDisplayName(name.italicLess)
                .addLoreLines("")
                .addLoreLines(Component.join(JoinConfiguration.newlines(), lore.map(Component::italicLess)))
                .addLoreLines("")
        )
}