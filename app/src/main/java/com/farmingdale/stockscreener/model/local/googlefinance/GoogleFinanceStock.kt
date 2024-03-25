package com.farmingdale.stockscreener.model.local.googlefinance

/**
 * Local data class for Google Finance stock information
 * @param symbol Stock symbol
 * @param name Stock name
 * @param current Current stock price
 * @param change Change in stock price
 * @param percentChange Percent change in stock price
 */
data class GoogleFinanceStock(
    val symbol: String,
    val name: String,
    val current: String,
    val change: String,
    val percentChange: String
)
