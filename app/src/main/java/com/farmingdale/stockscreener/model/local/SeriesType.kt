package com.farmingdale.stockscreener.model.local

/**
 * The available price types for technical analysis
 * @property OPEN open price
 * @property HIGH high price
 * @property LOW low price
 * @property CLOSE close price
 */
enum class SeriesType(val value: String) {
    OPEN("open"),
    HIGH("high"),
    LOW("low"),
    CLOSE("close")
}

