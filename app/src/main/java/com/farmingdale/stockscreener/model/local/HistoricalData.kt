package com.farmingdale.stockscreener.model.local

import androidx.compose.runtime.Immutable

/**
 * Local data class for a specific stock's OHLCV data at some date in time
 */
@Immutable
data class HistoricalData(
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long
)