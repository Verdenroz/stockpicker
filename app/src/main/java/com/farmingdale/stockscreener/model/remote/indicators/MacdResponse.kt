package com.farmingdale.stockscreener.model.remote.indicators

import com.farmingdale.stockscreener.model.local.indicators.Macd
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MacdResponse(
    @SerialName("MACD")
    val macd: Double,
    @SerialName("Signal")
    val signal: Double,
) {
    fun toMacd(): Macd {
        return Macd(
            macd = macd,
            signal = signal
        )
    }
}
