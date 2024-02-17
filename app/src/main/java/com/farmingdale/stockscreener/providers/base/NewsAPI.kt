package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News

/**
 * Base interface for NewsAPI
 * @see <a href="https://newsapi.org/docs/endpoints/top-headlines">NewsAPI Documentation</a>
 */
interface NewsAPI {
    /**
     * Get the latest news headlines by category or query
     * @param category the category of the news (business, entertainment, general, health, science, sports, technology)
     * @param query the search query
     * @return [News] containing the latest news headlines in a list
     */
    suspend fun getHeadlines(category: Category?, query: String?): News
}