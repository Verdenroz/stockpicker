package com.farmingdale.stockscreener.model.local.indicators

import androidx.compose.runtime.Immutable

@Immutable
data class BBands(
    val upperBand: Double,
    val lowerBand: Double
)