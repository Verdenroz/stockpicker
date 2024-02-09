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
    val changesPercentage: Double,
    val change: Double,
    val dayLow: Double,
    val dayHigh: Double,
    val yearHigh: Double,
    val yearLow: Double,
    val marketCap: Long,
    val priceAvg50: Double,
    val priceAvg200: Double,
    val volume: Long,
    val avgVolume: Long,
    val exchange: String,
    val open: Double,
    val previousClose: Double,
    val eps: Double,
    val pe: Double,
    val earningsAnnouncement: String,
    val sharesOutstanding: Long,
    val timestamp: Long
)