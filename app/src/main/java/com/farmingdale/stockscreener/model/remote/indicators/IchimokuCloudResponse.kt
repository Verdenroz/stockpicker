package com.farmingdale.stockscreener.model.remote.indicators

import com.farmingdale.stockscreener.model.local.indicators.IchimokuCloud
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IchimokuCloudResponse(
    @SerialName("Conversion Line")
    val conversionLine: Double,
    @SerialName("Base Line")
    val baseLine: Double,
    @SerialName("Lagging Span")
    val laggingSpan: Double? = null,
    @SerialName("Leading Span A")
    val leadingSpanA: Double? = null,
    @SerialName("Leading Span B")
    val leadingSpanB: Double? = null
) {
    fun toIchimokuCloud(): IchimokuCloud {
        return IchimokuCloud(
            conversionLine = conversionLine,
            baseLine = baseLine,
            laggingSpan = laggingSpan,
            leadingSpanA = leadingSpanA,
            leadingSpanB = leadingSpanB
        )
    }
}
