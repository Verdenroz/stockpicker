package com.farmingdale.stockscreener.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.SimpleQuoteData
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the quotes table.
 * Will be used to interact with local WatchList
 */
@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quoteData: DBQuoteData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(quotes: List<DBQuoteData>)

    @Query("DELETE FROM DBQuoteData WHERE symbol = :symbol")
    suspend fun delete(symbol: String)

    @Query("DELETE FROM DBQuoteData")
    suspend fun deleteAll()

    @Query("SELECT * FROM DBQuoteData")
    suspend fun getAllQuoteData(): List<SimpleQuoteData>

    @Query("SELECT * FROM DBQuoteData")
    fun getAllQuoteDataFlow(): Flow<List<SimpleQuoteData>>

}