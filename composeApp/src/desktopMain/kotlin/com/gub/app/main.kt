/**
 * Main entry point for Vehicle Detection Desktop Client
 * Author: Alims-Repo
 * Date: 2025-06-17
 */
package com.gub.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.gub.core.ui.theme.VehicleDetectionTheme
import java.awt.Toolkit

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {

    val (screenWidth, screenHeight) = getScreenDimensions()

    val windowWidth = screenWidth * 0.85f
    val windowHeight = screenHeight * 0.8f

    Window(
        onCloseRequest = {
            exitApplication()
        },
        title = "AI Vehicle Detection Desktop",
        state = rememberWindowState(
            width = windowWidth, height = windowHeight,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = true,
    ) {
        VehicleDetectionTheme {
            TrafficManagementApp()
        }
    }
}

fun getScreenDimensions() = Toolkit.getDefaultToolkit().screenSize.let { screenSize->
    Pair(screenSize.width.dp, screenSize.height.dp)
}