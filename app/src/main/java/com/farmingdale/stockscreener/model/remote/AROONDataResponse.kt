package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from AlphaVantage API for AROON technical analysis
 */
@Serializable
data class AROONDataResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Technical Analysis: AROON")
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
        @SerialName("Aroon Down")
        val aroonDown: String,

        @SerialName("Aroon Up")
        val aroonUp: String
    )
}