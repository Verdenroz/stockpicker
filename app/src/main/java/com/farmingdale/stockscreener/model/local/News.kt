package com.farmingdale.stockscreener.model.local

/**
 * Local data class for individual news articles
 * @param title: The title of the article
 * @param link: The link to the article
 * @param source: The source of the article
 * @param img: The image URL associated with the article
 * @param time: The time the article was published
 */
data class News(
    val title: String,
    val link: String,
    val source: String,
    val img: String,
    val time: String
)
