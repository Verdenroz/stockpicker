package com.farmingdale.stockscreener.model.remote.indicators

import com.farmingdale.stockscreener.model.local.indicators.SuperTrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuperTrendResponse(
    @SerialName("Super Trend")
    val superTrend: Double,
    @SerialName("Trend")
    val trend: String
) {
    fun toSuperTrend(): SuperTrend {
        return SuperTrend(
            superTrend = superTrend,
            trend = trend
        )
    }
}
