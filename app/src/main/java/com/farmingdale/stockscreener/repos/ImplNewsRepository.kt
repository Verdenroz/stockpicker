package com.farmingdale.stockscreener.repos

import android.content.Context
import android.content.SharedPreferences
import com.farmingdale.stockscreener.model.local.news.Category
import com.farmingdale.stockscreener.model.local.news.News
import com.farmingdale.stockscreener.providers.ImplNewsAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ImplNewsRepository(application: Context): NewsRepository() {
    private val api = ImplNewsAPI(okHttpClient)
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("NEWS_PREFERENCES", Context.MODE_PRIVATE)

    override suspend fun getHeadlines(category: Category?, query: String?): Flow<News> = flow{
        try{
            emit(api.getHeadlines(category, query))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.flowOn(Dispatchers.IO)

    override fun setPreferredQuery(query: String) {
        val editor = sharedPreferences.edit()
        editor.putString("preferredQuery", query)
        editor.apply()
    }

    override fun getPreferredQuery(): String? {
        return sharedPreferences.getString("preferredQuery", null)
    }

    override fun setPreferredCategory(category: Category) {
        val editor = sharedPreferences.edit()
        editor.putString("preferredCategory", category.name)
        editor.apply()
    }

    override fun getPreferredCategory(): Category? {
        val category = sharedPreferences.getString("preferredCategory", null)
        return if (category != null) Category.valueOf(category) else null
    }

    companion object{
        private var repo: NewsRepository? = null
        /**
         * Get the implementation of [ImplNewsAPI]
         */
        @Synchronized
        fun NewsRepository.Companion.get(context: Context): NewsRepository {
            if (ImplNewsRepository.repo == null) {
                ImplNewsRepository.repo = ImplNewsRepository(context)
            }

            return ImplNewsRepository.repo!!
        }
    }
}