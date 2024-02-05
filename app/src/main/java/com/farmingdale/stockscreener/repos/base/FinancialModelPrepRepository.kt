package com.farmingdale.stockscreener.repos.base

import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.GeneralSearchData
import com.farmingdale.stockscreener.model.local.SymbolList
import kotlinx.coroutines.flow.Flow

abstract class FinancialModelPrepRepository {
    abstract suspend fun generalSearch(query: String): Flow<GeneralSearchData>

    abstract suspend fun getSymbolList(): Flow<SymbolList>

    abstract suspend fun getFullQuote(symbol: String): Flow<FullQuoteData>
}