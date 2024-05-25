package com.farmingdale.stockscreener.model.local

data class HistoricalData(
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long
)