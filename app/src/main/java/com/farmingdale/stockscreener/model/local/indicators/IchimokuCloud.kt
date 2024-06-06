package com.farmingdale.stockscreener.model.local.indicators

data class IchimokuCloud(
    val conversionLine: Double,
    val baseLine: Double,
    val laggingSpan: Double?,
    val leadingSpanA: Double?,
    val leadingSpanB: Double?
)