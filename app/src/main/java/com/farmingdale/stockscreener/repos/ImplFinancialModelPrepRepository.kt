package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ImplFinancialModelPrepRepository: FinancialModelPrepRepository() {

    private val api  = ImplFinancialModelPrepAPI(okHttpClient)
    private val db = FirebaseFirestore.getInstance()

    override suspend fun generalSearch(query: String): Flow<GeneralSearchData> = flow{
        withContext(Dispatchers.IO){
            try{
                val result = api.generalSearch(query)
                emit(result)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override suspend fun getFullQuote(symbol: String): Flow<FullQuoteData> = flow{
        withContext(Dispatchers.IO){
            try{
                val result = api.getFullQuote(symbol)
                emit(result)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

    override suspend fun addToWatchList(symbol: String) {
        withContext(Dispatchers.IO){
            val quote = api.getFullQuote(symbol)
        }
    }

    override suspend fun deleteFromWatchList(symbol: String) {
        val quote = api.getFullQuote(symbol)
    }

    override suspend fun updateWatchList() {
        withContext(Dispatchers.IO){
            val watchList = db.collection("watchlist").get().await()
        }
    }

    companion object{
        private var repo: ImplFinancialModelPrepRepository? = null
        /**
         * Get the implementation of [ImplFinancialModelPrepRepository]
         */
        @Synchronized
        fun FinancialModelPrepRepository.Companion.get(): FinancialModelPrepRepository {
            if (repo == null) {
                repo = ImplFinancialModelPrepRepository()
            }

            return repo!!
        }
    }
}