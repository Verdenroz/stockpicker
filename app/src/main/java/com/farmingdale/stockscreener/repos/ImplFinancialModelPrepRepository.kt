package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SymbolList
import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.providers.okHttpClient
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImplFinancialModelPrepRepository: FinancialModelPrepRepository() {

    private val api  = ImplFinancialModelPrepAPI(okHttpClient)

    override suspend fun generalSearch(query: String): Flow<GeneralSearchData> = flow{
        try{
            val result = api.generalSearch(query)
            emit(result)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getFullQuote(symbol: String): Flow<FullQuoteData> = flow{
        try{
            val result = api.getFullQuote(symbol)
            emit(result)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun updateSymbolList(symbols: SymbolList) {
        TODO("Not yet implemented")
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