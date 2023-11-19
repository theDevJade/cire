/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package com.thedevjade.cire

import com.thedevjade.cire.items.EventProcesser
import com.thedevjade.cire.items.citem
import com.thedevjade.cire.resourcepacks.localResourcePack
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier


lateinit var plugin: Cire private set

@DefaultQualifier(NonNull::class)
class Cire : JavaPlugin() {
  override fun onEnable() {

    server.pluginManager.registerEvents(EventProcesser, this)

    plugin = this
    localResourcePack(true) {
      verbose = true
      httpdPort = 8080
      resourcePacks = mapOf(
        "pack" to dataFolder.resolve("pack.zip")
      )
    }

    val stackWithListeners = citem(Material.ACACIA_BOAT, "test") {
      lore {
        line("Test")
        line("Test2")
        line("&aColoredTest")
      }
      attributeBuilder(Attribute.GENERIC_MAX_HEALTH) {
        mode = AttributeModifier.Operation.MULTIPLY_SCALAR_1
        value = 5.0
        add()
      }

      interact { context ->
        val ev = context.event as PlayerInteractEvent
        ev.player.sendMessage("Test")
      }

      setName("&aWOWEEE")

      addEnchantment(Enchantment.DAMAGE_ALL, 5)
    }.build()
  }

  override fun onDisable() {}
}
