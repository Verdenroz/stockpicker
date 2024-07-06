package com.farmingdale.stockscreener.model.local.indicators

import androidx.compose.runtime.Immutable

@Immutable
data class SuperTrend(
    val superTrend: Double,
    val trend: String
)