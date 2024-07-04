package com.farmingdale.stockscreener.model.local.indicators

import androidx.compose.runtime.Immutable

@Immutable
data class Macd(
    val macd: Double,
    val signal: Double,
)
