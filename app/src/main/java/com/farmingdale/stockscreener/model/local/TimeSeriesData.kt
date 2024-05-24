package com.farmingdale.stockscreener.model.local

import java.time.LocalDate

data class HistoricalData(
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: Long
)

data class TimeSeriesData(
    val data: Map<LocalDate, HistoricalData>
)