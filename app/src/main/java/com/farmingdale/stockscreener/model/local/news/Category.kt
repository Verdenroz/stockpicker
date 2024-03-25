package com.farmingdale.stockscreener.model.local.news

/**
 * Represents the available categories for News API
 */
enum class Category(val displayName: String) {
    GENERAL("General"),
    BUSINESS("Business"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    SCIENCE("Science"),
    SPORTS("Sports"),
}