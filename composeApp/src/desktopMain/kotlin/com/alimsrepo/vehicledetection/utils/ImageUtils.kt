/**
 * Image utility functions
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.alimsrepo.vehicledetection.utils

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.util.*
import javax.imageio.ImageIO

object ImageUtils {
    
    fun base64ToBitmap(base64String: String): BufferedImage? {
        return try {
            val imageBytes = Base64.getDecoder().decode(base64String)
            val inputStream = ByteArrayInputStream(imageBytes)
            ImageIO.read(inputStream)
        } catch (e: Exception) {
            println("Error converting base64 to bitmap: ${e.message}")
            null
        }
    }
    
    fun bitmapToBase64(image: BufferedImage, format: String = "png"): String? {
        return try {
            val outputStream = java.io.ByteArrayOutputStream()
            ImageIO.write(image, format, outputStream)
            val imageBytes = outputStream.toByteArray()
            Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: Exception) {
            println("Error converting bitmap to base64: ${e.message}")
            null
        }
    }
    
    fun resizeImage(image: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val resized = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = resized.createGraphics()
        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null)
        graphics.dispose()
        return resized
    }
}