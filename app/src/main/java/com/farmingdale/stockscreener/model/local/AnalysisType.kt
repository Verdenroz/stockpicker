package com.farmingdale.stockscreener.model.local

/**
 * The technical indicators that can be used to analyze a stock
 * @param GLOBAL_QUOTE  the current price, volume, etc of stock
 * @param SMA  simple moving average
 * @param EMA  exponential moving average
 * @param STOCH  stochastic oscillator
 * @param RSI relative strength index
 * @param ADX  average directional movement index
 * @param CCI  commodity channel index
 * @param AROON  aroon
 * @param BBANDS  bollinger bands
 * @param AD  chaikin a/d line
 * @param OBV on balance volume
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