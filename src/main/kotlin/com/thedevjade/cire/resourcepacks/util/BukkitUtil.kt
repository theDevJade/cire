/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package network.aeternum.bananapuncher714.localresourcepackhoster.util

import java.io.IOException
import java.net.URL
import java.util.*

object BukkitUtil {
  private const val IP_URL = "https://api.ipify.org"
  val ip: String?
    get() = try {
      val url = URL(IP_URL)
      val stream = url.openStream()
      val s = Scanner(stream, "UTF-8").useDelimiter("\\A")
      val ip = s.next()
      s.close()
      stream.close()
      ip
    } catch (e: IOException) {
      e.printStackTrace()
      null
    }
}
