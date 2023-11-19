/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package com.thedevjade.cire.resourcepacks

import com.thedevjade.cire.resourcepacks.httpd.MineHttpd
import network.aeternum.bananapuncher714.localresourcepackhoster.util.BukkitUtil
import network.aeternum.bananapuncher714.localresourcepackhoster.util.Util
import network.aeternum.bananapuncher714.localresourcepackhoster.util.WebUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException
import java.security.NoSuchAlgorithmException

fun localResourcePack(localhost: Boolean, init: LocalResourcePack.() -> Unit): LocalResourcePack = LocalResourcePack(localhost).apply(init)

open class LocalResourcePack(private var localhost: Boolean) {
  var httpdPort = 0
  private var ip: String? = null
  private var httpd: MineHttpd? = null
  var verbose = false
  var resourcePacks: Map<String, File> = HashMap()

  init {
    startHttpd()
    ip = if (localhost) {
      Bukkit.getIp()
    } else {
      BukkitUtil.ip
    }
  }

  fun terminate() {
    httpd?.terminate()
  }

  private fun startHttpd() {
    try {
      httpd = object : MineHttpd(httpdPort) {
        override fun requestFileCallback(connection: MineHttpd.MineConnection, request: String): File? {
          val player: Player? = WebUtil.getAddress(connection)
          if (player == null) {
            verbose(
              "Unknown connection from '" + connection.client.getInetAddress() + "'. Aborting..."
            )
            return null
          }
          if (!resourcePacks.containsKey(request)) {
            return null
          }

          verbose(
            "Serving '" + request + "' to " + player.name + "(" + connection.client
              .getInetAddress() + ")"
          )
          return resourcePacks[request]
        }

        override fun onSuccessfulRequest(connection: MineHttpd.MineConnection, request: String) {
          verbose("Successfully served '" + request + "' to " + connection.client.getInetAddress())
        }

        override fun onClientRequest(connection: MineHttpd.MineConnection, request: String) {
          verbose("Request '" + request + "' recieved from " + connection.client.getInetAddress())
        }

        override fun onRequestError(connection: MineHttpd.MineConnection, code: Int) {
          verbose("Error " + code + " when attempting to serve " + connection.client.getInetAddress())
        }
      }
      // Start the web server
      httpd!!.start()
      println("Successfully started the mini http daemon!")
    } catch (e1: IOException) {
      println("Unable to start the mini http daemon! Disabling...")
    }
  }

  /**
   * Send a resource pack to the desired player
   *
   * @param player
   * The player to send it to, note that they must have the right permission to download the pack
   * @param resourcepack
   * The id of the resourcepack to send; must be valid
   */
  fun sendResourcePack(player: Player, resourcepack: String) {
    val file = resourcePacks[resourcepack]
    try {
      player.setResourcePack(
        "http://$ip:$httpdPort/$resourcepack",
        Util.calcSHA1(file),
        true
      )
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun verbose(`object`: Any) {
    if (verbose) {
      println(`object`.toString())
    }
  }
}
