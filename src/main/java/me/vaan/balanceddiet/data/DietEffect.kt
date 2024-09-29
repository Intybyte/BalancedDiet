package me.vaan.balanceddiet.data

import org.bukkit.potion.PotionEffect

data class DietEffect(
    val potionEffect: List<PotionEffect> = listOf(),
    val saturationMultiplier: Double = 1.0,
    val foodMultiplier: Double = 1.0,
    val damage: Double = 0.0
)