package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from AlphaVantage API for Stochastic Oscillator (STOCH) technical analysis
 */
@Serializable
data class STOCHDataResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Technical Analysis: STOCH")
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

        @SerialName("5.1: FastK Period")
        val fastKPeriod: Int,

        @SerialName("5.2: SlowK Period")
        val slowKPeriod: Int,

        @SerialName("5.3: SlowK MA Type")
        val slowKMAType: Int,

        @SerialName("5.4: SlowD Period")
        val slowDPeriod: Int,

        @SerialName("5.5: SlowD MA Type")
        val slowDMAType: Int,
    )

    @Serializable
    data class Analysis(
        val SlowK: String,
        val SlowD: String
    )
}