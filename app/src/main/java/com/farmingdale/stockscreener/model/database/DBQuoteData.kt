package com.farmingdale.stockscreener.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for FullQuoteData
 * Represents individual stock data in a WatchList
 */
@Entity
data class DBQuoteData(
    @PrimaryKey val symbol: String,
    val name: String,
    val price: Double,
    val change: String,
    val percentChange: String,
)