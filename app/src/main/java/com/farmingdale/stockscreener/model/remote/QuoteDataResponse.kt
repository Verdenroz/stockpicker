package com.farmingdale.stockscreener.model.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDataResponse(
    @SerialName("Global Quote")
    val globalQuote: GlobalQuote
) {
    @Serializable
    data class GlobalQuote(
        @SerialName("01. symbol")
        val symbol: String,
        @SerialName("02. open")
        val open: String,
        @SerialName("03. high")
        val high: String,
        @SerialName("04. low")
        val low: String,
        @SerialName("05. price")
        val price: String,
        @SerialName("06. volume")
        val volume: Int,
        @SerialName("07. latest trading day")
        val latestTradingDay: String,
        @SerialName("08. previous close")
        val previousClose: String,
        @SerialName("09. change")
        val change: String,
        @SerialName("10. change percent")
        val changePercent: String
    )
}