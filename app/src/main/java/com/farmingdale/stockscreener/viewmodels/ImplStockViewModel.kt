package com.farmingdale.stockscreener.viewmodels

import androidx.lifecycle.viewModelScope
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.repos.ImplFinanceQueryRepository.Companion.get
import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.viewmodels.base.StockViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImplStockViewModel(symbol: String): StockViewModel() {
    private val financeQueryRepo = FinanceQueryRepository.get()

    private val _timeSeries = MutableStateFlow<Map<String, HistoricalData>>(emptyMap())
    override val timeSeries: StateFlow<Map<String, HistoricalData>> = _timeSeries.asStateFlow()

    init {
        if (symbol.isNotEmpty()){
            getTimeSeries(symbol, TimePeriod.YEAR_TO_DATE, Interval.DAILY)
        }
    }

    override fun getTimeSeries(symbol: String, timePeriod: TimePeriod, interval: Interval) {
        viewModelScope.launch(Dispatchers.IO) {
            financeQueryRepo.getTimeSeries(symbol, timePeriod, interval).collect {
                _timeSeries.value = it
            }
        }
    }

}