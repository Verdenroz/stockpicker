package com.farmingdale.stockscreener.model.local

import androidx.compose.runtime.Immutable
import com.farmingdale.stockscreener.model.local.indicators.Aroon
import com.farmingdale.stockscreener.model.local.indicators.BBands
import com.farmingdale.stockscreener.model.local.indicators.IchimokuCloud
import com.farmingdale.stockscreener.model.local.indicators.Macd
import com.farmingdale.stockscreener.model.local.indicators.SuperTrend

/**
 * Represents the analysis of a stock that encompasses MAs, trends, and oscillators
 */
@Immutable
data class Analysis(
    val sma10: Double,
    val sma20: Double,
    val sma50: Double,
    val sma100: Double,
    val sma200: Double,
    val ema10: Double,
    val ema20: Double,
    val ema50: Double,
    val ema100: Double,
    val ema200: Double,
    val wma10: Double,
    val wma20: Double,
    val wma50: Double,
    val wma100: Double,
    val wma200: Double,
    val vwma20: Double,
    val rsi14: Double,
    val srsi14: Double,
    val cci20: Double,
    val adx14: Double,
    val macd: Macd,
    val stoch: Double,
    val aroon: Aroon,
    val bBands: BBands,
    val superTrend: SuperTrend,
    val ichimokuCloud: IchimokuCloud
)