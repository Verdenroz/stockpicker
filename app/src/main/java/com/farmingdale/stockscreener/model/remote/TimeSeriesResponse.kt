package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalDataResponse(
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long
)

@Serializable
data class TimeSeriesResponse(
    @SerialName("Historical Data")
    val data: Map<String, HistoricalDataResponse>
)