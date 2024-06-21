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
        val MOVING_AVERAGES: Set<AnalysisIndicators> = setOf(
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
        val OSCILLATORS: Set<AnalysisIndicators> = setOf(
            RSI,
            SRSI,
            STOCH,
            CCI,
        )
        val TRENDS: Set<AnalysisIndicators> = setOf(
            ADX,
            MACD,
            BBANDS,
            AROON,
            SUPERTREND,
            ICHIMOKUCLOUD
        )
    }
}