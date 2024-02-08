package com.farmingdale.stockscreener.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farmingdale.stockscreener.model.local.FullQuoteData
import kotlinx.coroutines.flow.Flow

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
    fun getAllFullQuoteData(): List<FullQuoteData>

    @Query("SELECT * FROM DBQuoteData")
    fun getAllFullQuoteDataFlow(): Flow<List<FullQuoteData>>

}