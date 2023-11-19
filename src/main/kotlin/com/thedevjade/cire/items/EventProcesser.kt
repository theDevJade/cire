/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package com.thedevjade.cire.items

import com.thedevjade.cire.plugin
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object EventProcesser : Listener {
  private fun getUniqueIdKey(): NamespacedKey {
    return NamespacedKey(plugin, "cire_id")
  }

  fun setUniqueIdentifier(item: ItemStack, id: String) {
    val meta = item.itemMeta
    meta.persistentDataContainer.set(getUniqueIdKey(), PersistentDataType.STRING, id)
    item.itemMeta = meta
  }

  fun getUniqueIdentifier(item: ItemStack): String? {
    val meta = item.itemMeta ?: return null
    return meta.persistentDataContainer.get(getUniqueIdKey(), PersistentDataType.STRING)
  }

  @EventHandler
  fun onPlayerInteract(event: PlayerInteractEvent) {
    val item = event.item ?: return

    val uniqueId = getUniqueIdentifier(item)

    if (ItemDirectory.containsKey(uniqueId)) {
      ItemDirectory[uniqueId]?.retrieveInteraction()?.let { it(ItemContext(event)) }
    }
  }
}
