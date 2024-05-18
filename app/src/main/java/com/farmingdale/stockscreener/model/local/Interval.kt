package com.farmingdale.stockscreener.model.local

/**
 * The available intervals for technical analysis and historical data
 */
enum class Interval(val value: String) {
    FIFTEEN_MINUTE("15m"),
    THIRTY_MINUTE("30m"),
    ONE_HOUR("1h"),
    DAILY("1d"),
    WEEKLY("1wk"),
    MONTHLY("1mo"),
    QUARTERLY("3mo")
}

/**
 * The available time periods for technical analysis and historical data
 */
enum class TimePeriod(val value: String) {
    ONE_DAY("1d"),
    FIVE_DAY("5d"),
    ONE_MONTH("1mo"),
    THREE_MONTH("3mo"),
    SIX_MONTH("6mo"),
    YEAR_TO_DATE("YTD"),
    ONE_YEAR("1Y"),
    FIVE_YEAR("5Y"),
    TEN_YEAR("10Y"),
    MAX("max")
}