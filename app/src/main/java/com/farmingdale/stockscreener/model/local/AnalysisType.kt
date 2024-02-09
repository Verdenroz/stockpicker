package com.farmingdale.stockscreener.model.local

/**
 * The technical indicators that can be used to analyze a stock
 * @property GLOBAL_QUOTE  the current price, volume, etc of stock
 * @property SMA  simple moving average
 * @property EMA  exponential moving average
 * @property STOCH  stochastic oscillator
 * @property RSI relative strength index
 * @property ADX  average directional movement index
 * @property CCI  commodity channel index
 * @property AROON  aroon
 * @property BBANDS  bollinger bands
 * @property AD  chaikin a/d line
 * @property OBV on balance volume
 */
enum class AnalysisType {
    GLOBAL_QUOTE,
    SMA,
    EMA,
    STOCH,
    RSI,
    ADX,
    CCI,
    AROON,
    BBANDS,
    AD,
    OBV,
}