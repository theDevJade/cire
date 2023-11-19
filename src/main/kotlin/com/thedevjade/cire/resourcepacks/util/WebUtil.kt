/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package network.aeternum.bananapuncher714.localresourcepackhoster.util

import com.thedevjade.cire.resourcepacks.httpd.MineHttpd
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException

object WebUtil {
  fun getAddress(connection: MineHttpd.MineConnection): Player? {
    val mac: ByteArray = connection.client.getInetAddress().address
    for (player in Bukkit.getOnlinePlayers()) {
      if (player.address.address.address.contentEquals(mac)) {
        return player
      }
    }
    return null
  }

  fun getMAC(address: InetAddress?): ByteArray? {
    return try {
      NetworkInterface.getByInetAddress(address).getHardwareAddress()
    } catch (e: SocketException) {
      e.printStackTrace()
      null
    }
  }
}
