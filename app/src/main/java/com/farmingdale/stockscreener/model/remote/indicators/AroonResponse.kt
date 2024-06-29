package com.farmingdale.stockscreener.model.remote.indicators

import com.farmingdale.stockscreener.model.local.indicators.Aroon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AroonResponse(
    @SerialName("Aroon Up")
    val aroonUp: Double,
    @SerialName("Aroon Down")
    val aroonDown: Double
) {
    fun toAroon(): Aroon {
        return Aroon(
            aroonUp = aroonUp,
            aroonDown = aroonDown
        )
    }
}

