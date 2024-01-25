package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.AnalysisType
import com.farmingdale.stockscreener.model.local.QuoteData
import com.farmingdale.stockscreener.model.local.TechnicalAnalysis
import com.farmingdale.stockscreener.model.local.TechnicalAnalysisHistory

interface AlphaVantageAPI {

    suspend fun getQuote(symbol: String): QuoteData

    suspend fun getSMA(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    suspend fun getEMA(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    suspend fun getSTOCH(
        function: AnalysisType,
        symbol: String,
        interval: String,
        fastKPeriod: Int?,
        slowKPeriod: Int?,
        slowDPeriod: Int?,
        slowKMAType: Int?,
        slowDMAType: Int?
    ): TechnicalAnalysisHistory

    suspend fun getRSI(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    suspend fun getADX(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    suspend fun getCCI(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    suspend fun getAROON(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    suspend fun getBBANDS(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String,
        nbDevUp: Int?,
        nbDevDown: Int?,
        matype: Int?
    ): TechnicalAnalysisHistory

    suspend fun getAD(
        function: AnalysisType,
        symbol: String,
        interval: String
    ): TechnicalAnalysisHistory

    suspend fun getOBV(
        function: AnalysisType,
        symbol: String,
        interval: String
    ): TechnicalAnalysisHistory


}