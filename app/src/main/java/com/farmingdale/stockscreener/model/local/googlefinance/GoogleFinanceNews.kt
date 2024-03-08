package com.farmingdale.stockscreener.model.local.googlefinance

import java.net.URL

/**
 * Local data class for Google Finance news
 * @param headline News headline
 * @param image URL to the image
 * @param source Source of the article
 * @param url Link to the article
 */
data class GoogleFinanceNews(
    val headline: String,
    val image: URL,
    val source: String,
    val url: URL
)
