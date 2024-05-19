package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricalDataResponse(
    val open: String,
    val high: String,
    val low: String,
    val adj_close: String,
    val volume: Long
)

@Serializable
data class TimeSeriesResponse(
    @SerialName("Historical Data")
    val data: Map<String, HistoricalDataResponse>
)