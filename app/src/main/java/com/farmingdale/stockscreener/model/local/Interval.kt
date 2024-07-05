package com.farmingdale.stockscreener.model.local

import androidx.compose.runtime.Stable

/**
 * The available intervals for technical analysis and historical data
 */
@Stable
enum class Interval(val value: String) {
    ONE_MINUTE("1m"),
    FIVE_MINUTE("5m"),
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
@Stable
enum class TimePeriod(val value: String) {
    ONE_DAY("1d"),
    FIVE_DAY("5d"),
    ONE_WEEK("7d"),
    ONE_MONTH("1mo"),
    THREE_MONTH("3mo"),
    SIX_MONTH("6mo"),
    YEAR_TO_DATE("YTD"),
    ONE_YEAR("1Y"),
    FIVE_YEAR("5Y"),
    TEN_YEAR("10Y"),
    MAX("max")
}