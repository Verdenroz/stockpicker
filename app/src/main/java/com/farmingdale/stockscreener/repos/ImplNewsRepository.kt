package com.farmingdale.stockscreener.repos

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.providers.ImplNewsAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImplNewsRepository(application: Context): NewsRepository() {
    private val api = ImplNewsAPI(okHttpClient)
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("NEWS_PREFERENCES", Context.MODE_PRIVATE)
    private val headlinesFlow = MutableStateFlow<News?>(null)

    override val headlines: Flow<News?> = flow {
        while (true) {
            val category = getPreferredCategory()
            headlinesFlow.value = api.getHeadlines(category)
            emit(headlinesFlow.value)
            delay(60000)
        }
    }.flowOn(Dispatchers.IO)

    override fun setPreferredCategory(category: Category) {
        val editor = sharedPreferences.edit()
        editor.putString("preferredCategory", category.name)
        editor.apply()
    }

    override fun getPreferredCategory(): Category? {
        val category = sharedPreferences.getString("preferredCategory", null)
        return if (category != null) Category.valueOf(category) else null
    }

    override suspend fun refreshHeadlines() {
        Log.d("ImplNewsRepository", "Refreshing news")
        coroutineScope {
            val headlinesDeferred = async(Dispatchers.IO) { api.getHeadlines(getPreferredCategory()) }

            headlinesFlow.value = headlinesDeferred.await()
            headlinesFlow.emit(headlinesFlow.value)
        }
    }

    companion object{
        private var repo: NewsRepository? = null
        /**
         * Get the implementation of [ImplNewsAPI]
         */
        @Synchronized
        fun NewsRepository.Companion.get(context: Context): NewsRepository {
            if (repo == null) {
                repo = ImplNewsRepository(context)
            }

            return repo!!
        }
    }
}