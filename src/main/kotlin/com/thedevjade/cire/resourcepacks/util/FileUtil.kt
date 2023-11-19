/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

package network.aeternum.bananapuncher714.localresourcepackhoster.util

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.*

/**
 * Simple file management including YAML updaters
 *
 * @author BananaPuncher714
 */
object FileUtil {
  const val BUFFER_SIZE = 8192
  fun saveToFile(stream: InputStream, output: File, force: Boolean) {
    if (force && output.exists()) output.delete()
    if (!output.exists()) {
      output.getParentFile().mkdirs()
      try {
        val buffer = ByteArray(stream.available())
        val outStream: OutputStream = FileOutputStream(output)
        var len: Int
        while (stream.read(buffer).also { len = it } > 0) {
          outStream.write(buffer, 0, len)
        }
        stream.close()
        outStream.close()
      } catch (exception: Exception) {
        exception.printStackTrace()
      }
    }
  }

  @JvmOverloads
  fun updateConfigFromFile(toUpdate: File?, toCopy: InputStream?, trim: Boolean = false) {
    val config: FileConfiguration = YamlConfiguration.loadConfiguration(InputStreamReader(toCopy))
    val old: FileConfiguration = YamlConfiguration.loadConfiguration(toUpdate!!)
    for (key in config.getKeys(true)) {
      if (!old.contains(key!!)) {
        old[key] = config[key]
      }
    }
    if (trim) {
      for (key in old.getKeys(true)) {
        if (!config.contains(key!!)) {
          old[key] = null
        }
      }
    }
    try {
      old.save(toUpdate)
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }

  fun move(original: File, dest: File, force: Boolean): Boolean {
    if (dest.exists()) {
      if (!force) {
        return false
      } else {
        recursiveDelete(dest)
      }
    }
    dest.getParentFile().mkdirs()
    original.renameTo(dest)
    recursiveDelete(original)
    return true
  }

  fun recursiveDelete(file: File) {
    if (!file.exists()) {
      return
    }
    if (file.isDirectory()) {
      for (lower in file.listFiles()) {
        recursiveDelete(lower)
      }
    }
    file.delete()
  }

  fun <T> readObject(clazz: Class<T>?, file: File?): T? {
    var head: T? = null
    try {
      val fis = FileInputStream(file)
      val ois = ObjectInputStream(fis)
      head = ois.readObject() as T
      ois.close()
      fis.close()
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
    return head
  }

  fun writeObject(`object`: Serializable?, file: File?) {
    try {
      val fos = FileOutputStream(file)
      val oos = ObjectOutputStream(fos)
      oos.writeObject(`object`)
      oos.close()
      fos.close()
    } catch (exception: Exception) {
      exception.printStackTrace()
    }
  }
}
