package com.farmingdale.stockscreener.model.remote

import com.farmingdale.stockscreener.model.remote.indicators.AroonResponse
import com.farmingdale.stockscreener.model.remote.indicators.BBandsResponse
import com.farmingdale.stockscreener.model.remote.indicators.IchimokuCloudResponse
import com.farmingdale.stockscreener.model.remote.indicators.MacdResponse
import com.farmingdale.stockscreener.model.remote.indicators.SuperTrendResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalysisResponse(
    val symbol: String,
    @SerialName("SMA(10)")
    val sma10: Double,
    @SerialName("SMA(20)")
    val sma20: Double,
    @SerialName("SMA(50)")
    val sma50: Double,
    @SerialName("SMA(100)")
    val sma100: Double,
    @SerialName("SMA(200)")
    val sma200: Double,
    @SerialName("EMA(10)")
    val ema10: Double,
    @SerialName("EMA(20)")
    val ema20: Double,
    @SerialName("EMA(50)")
    val ema50: Double,
    @SerialName("EMA(100)")
    val ema100: Double,
    @SerialName("EMA(200)")
    val ema200: Double,
    @SerialName("WMA(10)")
    val wma10: Double,
    @SerialName("WMA(20)")
    val wma20: Double,
    @SerialName("WMA(50)")
    val wma50: Double,
    @SerialName("WMA(100)")
    val wma100: Double,
    @SerialName("WMA(200)")
    val wma200: Double,
    @SerialName("VWMA(20)")
    val vwma20: Double,
    @SerialName("RSI(14)")
    val rsi14: Double,
    @SerialName("SRSI(3,3,14,14)")
    val srsi14: Double,
    @SerialName("CCI(20)")
    val cci20: Double,
    @SerialName("ADX(14)")
    val adx14: Double,
    @SerialName("MACD(12,26)")
    val macd: MacdResponse,
    @SerialName("STOCH %K(14,3,3)")
    val stoch: Double,
    @SerialName("Aroon(25)")
    val aroon: AroonResponse,
    @SerialName("BBANDS(20,2)")
    val bBands: BBandsResponse,
    @SerialName("Super Trend")
    val superTrend: SuperTrendResponse,
    @SerialName("Ichimoku Cloud")
    val ichimokuCloud: IchimokuCloudResponse
)







