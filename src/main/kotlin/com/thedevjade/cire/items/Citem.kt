/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package com.thedevjade.cire.items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

val ItemDirectory: MutableMap<String, Citem> = HashMap()

fun citem(material: Material, id: String, init: Citem.() -> Unit): Citem = Citem(material, id).apply(init)

@Suppress("Unused")
class Citem(private val material: Material, val id: String) {
  val name: String = id

  private val stack: ItemStack = ItemStack(material)
  private lateinit var interact: (context: ItemContext) -> Unit

  fun addLore(lore: String, colorChar: Char = '&') {
    stack.itemMeta.lore()?.add(Component.text(lore.replace(colorChar, '§')))
  }

  fun removeLore(line: Int) {
    stack.itemMeta.lore()?.removeAt(line)
  }

  fun removeLore(lore: String) {
    stack.itemMeta.lore()?.removeIf { it.toString() == lore.replace('&', '§') }
  }

  fun lore(builder: LoreBuilder.() -> Unit): LoreBuilder = LoreBuilder().apply(builder)

  inner class LoreBuilder {
    private var lines = mutableListOf<String>()

    fun line(line: String) {
      lines.add(line)
    }

    fun modify(index: Int, newLine: String) {
      lines[index] = newLine
    }

    fun modify(oldLine: String, newLine: String) {
      lines[lines.indexOf(oldLine)] = newLine
    }

    fun remove(index: Int) {
      lines.removeAt(index)
    }

    fun remove(line: String) {
      lines.remove(line)
    }

    fun replace(list: MutableList<String>) {
      lines = list
    }

    fun insert(list: MutableList<String>) {
      lines.addAll(list)
    }

    fun build() {
      stack.itemMeta.lore(lines.map { Component.text(it.replace('&', '§')) })
    }
  }

  fun setName(name: String) {
    stack.itemMeta.displayName(Component.text(name.replace('&', '§')))
  }

  fun addEnchantment(enchant: Enchantment, lvl: Int = 1) {
    stack.addEnchantment(enchant, lvl)
  }

  fun removeEnchantment(enchant: Enchantment) {
    stack.itemMeta.removeEnchant(enchant)
  }

  fun attributeBuilder(attribute: Attribute, builder: AttributeBuilder.() -> Unit): AttributeBuilder = AttributeBuilder(attribute).apply(builder)

  inner class AttributeBuilder(val attribute: Attribute) {
    var mode: AttributeModifier.Operation = AttributeModifier.Operation.ADD_NUMBER
    var value: Double = 1.0

    fun add() {
      stack.itemMeta.addAttributeModifier(attribute, AttributeModifier(id, value, mode))
    }
  }

  fun interact(
    block: (
      context: ItemContext
    ) -> Unit
  ) {
    interact = block
  }

  fun retrieveInteraction(): (context: ItemContext) -> Unit {
    return interact
  }

  fun build(): ItemStack {
    EventProcesser.setUniqueIdentifier(stack, id)
    ItemDirectory[id] = this
    return stack
  }

  fun getMaterial(): Material = material
}

class ItemContext(val event: Event)


