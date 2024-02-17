package com.farmingdale.stockscreener.model.local.news

/**
 * Local data class for individual news articles
 * @param source: The source of the article
 * @param author: The author of the article
 * @param title: The title of the article
 * @param description: Short description of the article
 * @param url: The url of the article
 * @param urlToImage: The url to the image of the article
 * @param publishedAt: The date the article was published
 * @param content: The content of the article. Often cut off in the API response.
 */
data class Article(
    val source: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)
