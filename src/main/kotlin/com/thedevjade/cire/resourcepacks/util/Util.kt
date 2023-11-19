/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package network.aeternum.bananapuncher714.localresourcepackhoster.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object Util {
  @Throws(IOException::class, NoSuchAlgorithmException::class)
  fun calcSHA1String(file: File?): String {
    return bytesToHexString(calcSHA1(file))
  }

  @Throws(IOException::class, NoSuchAlgorithmException::class)
  fun calcSHA1(file: File?): ByteArray {
    val fileInputStream = FileInputStream(file)
    val digest = MessageDigest.getInstance("SHA-1")
    val digestInputStream = DigestInputStream(fileInputStream, digest)
    val bytes = ByteArray(1024)
    // read all file content
    while (digestInputStream.read(bytes) > 0);
    val resultByteArry = digest.digest()
    digestInputStream.close()
    return resultByteArry
  }

  fun bytesToHexString(bytes: ByteArray): String {
    val sb = StringBuilder()
    for (b in bytes) {
      val value = b.toInt() and 0xFF
      if (value < 16) {
        // if value less than 16, then it's hex String will be only
        // one character, so we need to append a character of '0'
        sb.append("0")
      }
      sb.append(Integer.toHexString(value).uppercase(Locale.getDefault()))
    }
    return sb.toString()
  }

  fun saveFile(url: String?, file: File) {
    try {
      file.getParentFile().mkdirs()
      val website = URL(url)
      val rbc = Channels.newChannel(website.openStream())
      val fos = FileOutputStream(file)
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE)
      fos.close()
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  fun isYoutubeURL(url: String): Boolean {
    return url.matches("^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+".toRegex())
  }

  fun recursiveDelete(file: File) {
    if (file.isDirectory()) {
      for (element in file.listFiles()) {
        recursiveDelete(element)
      }
    }
    file.delete()
  }
}
