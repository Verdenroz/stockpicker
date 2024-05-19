package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * Data response for news
 * @param title News title
 * @param link URL to the news
 * @param source News source
 * @param img Image URL associated with the news
 * @param time Time the news was published
 */
@Serializable
data class NewsResponse(
    val title: String,
    val link: String,
    val source: String,
    val img: String,
    val time: String
)
