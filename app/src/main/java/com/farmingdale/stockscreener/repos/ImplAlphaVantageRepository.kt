package com.farmingdale.stockscreener.repos

import com.farmingdale.stockscreener.model.local.QuoteData
import com.farmingdale.stockscreener.model.local.SearchData
import com.farmingdale.stockscreener.model.local.TechnicalAnalysisHistory
import com.farmingdale.stockscreener.providers.ImplAlphaVantageAPI
import com.farmingdale.stockscreener.repos.base.AlphaVantageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImplAlphaVantageRepository(private val api: ImplAlphaVantageAPI): AlphaVantageRepository(){
    override fun getQuote(symbol: String): Flow<QuoteData> {
        TODO("Not yet implemented")
    }

    override fun querySymbols(query: String): Flow<SearchData> = flow {
        try{
            val result = api.searchSymbol(query)
            emit(result)
        } catch (e: Exception){
            e.printStackTrace()
        }

    }

    override fun getTechnicalAnalysis(symbol: String): Flow<TechnicalAnalysisHistory> {
        TODO("Not yet implemented")
    }
}