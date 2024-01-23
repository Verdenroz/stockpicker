package com.farmingdale.stockscreener.model.local

enum class Interval(val value: String) {
    ONE_MINUTE("1min"),
    FIVE_MINUTE("5min"),
    FIFTEEN_MINUTE("15min"),
    THIRTY_MINUTE("30min"),
    SIXTY_MINUTE("60min"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly")
}