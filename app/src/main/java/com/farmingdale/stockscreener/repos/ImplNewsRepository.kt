package com.farmingdale.stockscreener.repos

import android.content.Context
import android.content.SharedPreferences
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.providers.ImplNewsAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.withContext

class ImplNewsRepository(application: Context) : NewsRepository() {
    private val api = ImplNewsAPI(okHttpClient)
    private val refreshInterval = 60000L
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("NEWS_PREFERENCES", Context.MODE_PRIVATE)
    private val _headlinesFlow = MutableStateFlow<News?>(null)

    init {
        refreshHeadlinesPeriodically()
    }

    override val headlines: Flow<News?> = _headlinesFlow.asStateFlow()
    override fun setPreferredCategory(category: Category) {
        val editor = sharedPreferences.edit()
        editor.putString("preferredCategory", category.name)
        editor.apply()
    }

    override fun getPreferredCategory(): Category? {
        val category = sharedPreferences.getString("preferredCategory", Category.BUSINESS.name)
        return if (category != null) Category.valueOf(category) else null
    }

    override suspend fun refreshHeadlines() {
        withContext(Dispatchers.IO) {
            _headlinesFlow.emit(
                News(
                    api.getHeadlines(getPreferredCategory()).articles.shuffled().take(10)
                )
            )
        }
    }

    private fun refreshHeadlinesPeriodically() = flow<Unit> {
        while (true) {
            refreshHeadlines()
            delay(refreshInterval)
        }
    }.flowOn(Dispatchers.IO).launchIn(CoroutineScope(Dispatchers.IO))

    companion object {
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