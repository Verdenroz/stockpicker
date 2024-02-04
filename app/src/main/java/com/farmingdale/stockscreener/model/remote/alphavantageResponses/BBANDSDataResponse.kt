package com.farmingdale.stockscreener.model.remote.alphavantageResponses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from AlphaVantage API for Boillinger Bands (BBANDS) technical analysis
 */
@Serializable
data class BBANDSDataResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Technical Analysis: BBANDS")
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

        @SerialName("6.1: Deviation multiplier for upper band")
        val deviationMultiplierForUpperBand: Int,

        @SerialName("6.2: Deviation multiplier for lower band")
        val deviationMultiplierForLowerBand: Int,

        @SerialName("6.3: MA Type")
        val maType: Int,
    )

    @Serializable
    data class Analysis(
        @SerialName("Real Upper Band")
        val realUpperBand: String,

        @SerialName("Real Middle Band")
        val realMiddleBand: String,

        @SerialName("Real Lower Band")
        val realLowerBand: String
    )
}