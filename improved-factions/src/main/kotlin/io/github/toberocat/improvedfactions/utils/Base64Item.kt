package io.github.toberocat.improvedfactions.utils

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object Base64Item {
    fun encode(item: ItemStack?): String {
        ByteArrayOutputStream().use { outputStream ->
            BukkitObjectOutputStream(outputStream).use { dataOutput ->
                dataOutput.writeObject(item)
                return String(Base64Coder.encode(outputStream.toByteArray()))
            }
        }
    }

    fun decode(base64: String): ItemStack? {
        ByteArrayInputStream(Base64Coder.decode(base64)).use { inputStream ->
            BukkitObjectInputStream(inputStream).use { dataInput ->
                return dataInput.readObject() as ItemStack?
            }
        }
    }
}