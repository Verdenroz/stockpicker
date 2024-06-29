package com.farmingdale.stockscreener.model.remote.indicators

import com.farmingdale.stockscreener.model.local.indicators.BBands
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BBandsResponse(
    @SerialName("Upper Band")
    val upperBand: Double,
    @SerialName("Lower Band")
    val lowerBand: Double
) {
    fun toBBands(): BBands {
        return BBands(
            upperBand = upperBand,
            lowerBand = lowerBand
        )
    }
}