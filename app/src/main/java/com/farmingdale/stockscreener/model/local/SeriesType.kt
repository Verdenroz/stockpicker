package com.farmingdale.stockscreener.model.local

/**
 * The available price types for technical analysis
 * @param OPEN open price
 * @param HIGH high price
 * @param LOW low price
 * @param CLOSE close price
 */
enum class SeriesType(val value: String) {
    OPEN("open"),
    HIGH("high"),
    LOW("low"),
    CLOSE("close")
}

