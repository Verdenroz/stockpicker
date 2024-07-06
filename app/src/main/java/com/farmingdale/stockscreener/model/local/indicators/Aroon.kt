package com.farmingdale.stockscreener.model.local.indicators

import androidx.compose.runtime.Immutable

@Immutable
data class Aroon(
    val aroonUp: Double,
    val aroonDown: Double
)