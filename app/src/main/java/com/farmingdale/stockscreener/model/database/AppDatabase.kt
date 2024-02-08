package com.farmingdale.stockscreener.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DBQuoteData::class
               ],
    version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun get(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "whereswhat.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}
