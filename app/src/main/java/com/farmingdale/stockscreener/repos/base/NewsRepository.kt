package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import kotlinx.coroutines.flow.Flow

abstract class NewsRepository {

    /**
     * Get relevant headlines based on optional category and query
     * @param category [Category] of the headlines
     * @param query Optional to narrow headlines to keywords
     * @return Flow of [News]
     */
    abstract suspend fun getHeadlines(category: Category?, query: String?): Flow<News>

    /**
     * Set preferred query in shared preferences
     */
    abstract fun setPreferredQuery(query: String)

    /**
     * Get preferred query from shared preferences
     */
    abstract fun getPreferredQuery(): String?

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