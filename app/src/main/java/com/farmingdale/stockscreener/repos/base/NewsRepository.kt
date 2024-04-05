package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import kotlinx.coroutines.flow.Flow

abstract class NewsRepository {

    /**
     * Get relevant headlines based on optional category and query
     * @return Flow of [News]
     */
    abstract val headlines: Flow<News?>

    /**
     * Set preferred category in shared preferences
     */
    abstract fun setPreferredCategory(category: Category)

    /**
     * Get preferred category from shared preferences
     */
    abstract fun getPreferredCategory(): Category?

    abstract suspend fun refreshHeadlines()

    companion object

}