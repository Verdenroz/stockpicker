package com.farmingdale.stockscreener.model.local.indicators

import androidx.compose.runtime.Immutable

@Immutable
data class IchimokuCloud(
    val conversionLine: Double,
    val baseLine: Double,
    val laggingSpan: Double?,
    val leadingSpanA: Double?,
    val leadingSpanB: Double?
)