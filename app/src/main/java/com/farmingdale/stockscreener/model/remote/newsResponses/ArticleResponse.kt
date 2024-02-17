package com.farmingdale.stockscreener.model.remote.newsResponses

import kotlinx.serialization.Serializable

/**
 * Data response for articles
 * @param source source of the article
 * @param author author of the article
 * @param title title of the article
 * @param description description of the article
 * @param url link to the article
 * @param urlToImage link to the image of the article
 * @param publishedAt date of the article
 * @param content content of the article
 */
@Serializable
class ArticleResponse(
    val source: SourceResponse,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)