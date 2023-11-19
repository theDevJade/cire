/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package com.thedevjade.cire

import com.thedevjade.cire.resourcepacks.localResourcePack
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier

@DefaultQualifier(NonNull::class)
class Cire : JavaPlugin() {
  override fun onEnable() {
    localResourcePack(true) {
      verbose = true
      httpdPort = 8080
      resourcePacks = mapOf(
        "pack" to dataFolder.resolve("pack.zip")
      )
    }
  }

  override fun onDisable() {}
}
