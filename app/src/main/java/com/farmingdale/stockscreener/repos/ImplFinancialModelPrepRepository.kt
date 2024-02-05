package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SymbolList
import com.farmingdale.stockscreener.providers.ImplFinancialModelPrepAPI
import com.farmingdale.stockscreener.repos.base.FinancialModelPrepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImplFinancialModelPrepRepository(private val api: ImplFinancialModelPrepAPI): FinancialModelPrepRepository() {
    override suspend fun generalSearch(query: String): Flow<GeneralSearchData> = flow{
        try{
            val result = api.generalSearch(query)
            emit(result)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override suspend fun getSymbolList(): Flow<SymbolList> {
        TODO("Not yet implemented")
    }

    override suspend fun getFullQuote(symbol: String): Flow<FullQuoteData> {
        TODO("Not yet implemented")
    }

}