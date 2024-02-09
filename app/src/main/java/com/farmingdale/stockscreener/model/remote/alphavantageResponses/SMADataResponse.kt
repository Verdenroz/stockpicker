package com.farmingdale.stockscreener.model.remote.alphavantageResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from AlphaVantage API for Simple Moving Average (SMA) technical analysis
 */
@Serializable
data class SMADataResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Technical Analysis: SMA")
    val technicalAnalysis: Map<String, Analysis>
) {
    @Serializable
    data class MetaData(
        @SerialName("1: Symbol")
        val symbol: String,

        @SerialName("2: Indicator")
        val indicator: String,

        @SerialName("3: Last Refreshed")
        val lastRefreshed: String,
    )

    @Serializable
    data class Analysis(
        val SMA: String
    )
}