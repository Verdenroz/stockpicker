package com.farmingdale.stockscreener.providers

import com.farmingdale.stockscreener.BuildConfig
import com.farmingdale.stockscreener.model.local.news.Article
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.providers.base.NewsAPI
import com.farmingdale.stockscreener.utils.NEWS_API_URL
import com.farmingdale.stockscreener.utils.executeAsync
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class ImplNewsAPI(private val client: OkHttpClient): NewsAPI {

    companion object {
        private val parser: Json by lazy {
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }
    }
    private suspend fun getByteStream(url: HttpUrl): InputStream {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = client.newCall(request)
        val response = call.executeAsync()
        return response.body!!.byteStream()
    }
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getHeadlines(category: Category?): News {
        val stream = getByteStream(
            NEWS_API_URL.newBuilder().apply{
                addPathSegments("top-headlines")
                addQueryParameter("country", "us")
                category?.let { addQueryParameter("category", it.name) }
                addQueryParameter("apiKey", BuildConfig.newsAPIKey)
            }.build()
        )

        val newsResponse = parser.decodeFromStream(NewsResponse.serializer(), stream)

        val articles = newsResponse.articles.filter{
            it.description != null && it.urlToImage != null && !it.source.name.isNullOrBlank()
        }.map{ article ->
            Article(
                source = article.source.name!!,
                author = article.author,
                title = article.title,
                description = article.description,
                url = article.url,
                urlToImage = article.urlToImage,
                publishedAt = article.publishedAt,
                content = article.content
            )
        }.take(10).shuffled()
        return News(articles)
    }
}