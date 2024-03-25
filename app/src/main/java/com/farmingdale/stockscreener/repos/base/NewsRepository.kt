package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import kotlinx.coroutines.flow.Flow

abstract class NewsRepository {

    /**
     * Get relevant headlines based on optional category and query
     * @param category [Category] of the headlines
     * @return Flow of [News]
     */
    abstract suspend fun getHeadlines(category: Category?): Flow<News>

    /**
     * Set preferred category in shared preferences
     */
    abstract fun setPreferredCategory(category: Category)

    /**
     * Get preferred category from shared preferences
     */
    abstract fun getPreferredCategory(): Category?

    companion object

}