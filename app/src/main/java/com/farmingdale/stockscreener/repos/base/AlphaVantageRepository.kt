package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.QuoteData
import com.farmingdale.stockscreener.model.local.SearchData
import com.farmingdale.stockscreener.model.local.TechnicalAnalysisHistory
import kotlinx.coroutines.flow.Flow

abstract class AlphaVantageRepository {

    abstract fun getQuote(symbol: String): Flow<QuoteData>

    abstract fun querySymbols(query: String): Flow<SearchData>

    abstract fun getTechnicalAnalysis(symbol: String): Flow<TechnicalAnalysisHistory>


}