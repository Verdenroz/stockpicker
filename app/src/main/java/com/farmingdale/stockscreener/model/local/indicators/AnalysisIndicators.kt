package com.farmingdale.stockscreener.model.local.indicators

enum class AnalysisIndicators {
    SMA10,
    SMA20,
    SMA50,
    SMA100,
    SMA200,
    EMA10,
    EMA20,
    EMA50,
    EMA100,
    EMA200,
    WMA10,
    WMA20,
    WMA50,
    WMA100,
    WMA200,
    VWMA20,
    RSI,
    SRSI,
    CCI,
    ADX,
    MACD,
    STOCH,
    AROON,
    BBANDS,
    SUPERTREND,
    ICHIMOKUCLOUD;

    companion object {
        val MOVING_AVERAGES: List<AnalysisIndicators> = listOf(
            SMA10,
            SMA20,
            SMA50,
            SMA100,
            SMA200,
            EMA10,
            EMA20,
            EMA50,
            EMA100,
            EMA200,
            WMA10,
            WMA20,
            WMA50,
            WMA100,
            WMA200,
            VWMA20
        )
        val OSCILLATORS: List<AnalysisIndicators> = listOf(
            RSI,
            SRSI,
            STOCH,
            CCI,
        )
        val TRENDS: List<AnalysisIndicators> = listOf(
            ADX,
            MACD,
            BBANDS,
            AROON,
            SUPERTREND,
            ICHIMOKUCLOUD
        )
    }
}