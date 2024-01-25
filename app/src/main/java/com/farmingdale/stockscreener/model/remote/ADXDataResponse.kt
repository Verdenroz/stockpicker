package com.farmingdale.stockscreener.model.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ADXDataResponse(
    @SerialName("Meta Data")
    val metaData: MetaData,

    @SerialName("Technical Analysis: ADX")
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
        val ADX: String
    )
}