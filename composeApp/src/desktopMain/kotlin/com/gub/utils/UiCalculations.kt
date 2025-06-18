package com.gub.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

object UiCalculations {

    @Composable
    fun Int.toDp(): Dp {
        val density = LocalDensity.current
        return with(density) { this@toDp.toDp() }
    }

    fun Int.toDp(density: Density): Dp {
        return with(density) { this@toDp.toDp() }
    }
}