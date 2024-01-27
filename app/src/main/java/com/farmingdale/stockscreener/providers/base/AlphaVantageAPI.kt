package com.farmingdale.stockscreener.providers.base

import com.farmingdale.stockscreener.model.local.AnalysisType
import com.farmingdale.stockscreener.model.local.QuoteData
import com.farmingdale.stockscreener.model.local.TechnicalAnalysis
import com.farmingdale.stockscreener.model.local.TechnicalAnalysisHistory

/**
 * Base interface for AlphaVantageAPI
 * @see <a href="https://www.alphavantage.co/documentation/">AlphaVantage API Documentation</a>
 */
interface AlphaVantageAPI {

    /**
     * Get the latest price and volume information for a security of your choice.
     * @param symbol the symbol of the security
     */
    suspend fun getQuote(symbol: String): QuoteData

    /**
     * Get Simple Moving Average (SMA) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod the number of data points used to calculate each moving average value (positive integers only)
     * @param seriesType the desired price type in the time series (close, open, high, low)
     * @return a list of SMA values with dates
     */
    suspend fun getSMA(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    /**
     * Get Exponential Moving Average (EMA) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod the number of data points used to calculate each moving average value (positive integers only)
     * @param seriesType the desired price type in the time series (close, open, high, low)
     * @return a list of EMA values with dates
     */
    suspend fun getEMA(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    /**
     * Get Stochastic oscillator (STOCH) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param fastKPeriod the time period of the fastK moving average (positive integers only). Default to 5
     * @param slowKPeriod the time period of the slowK moving average (positive integers only). Default to 3
     * @param slowDPeriod the time period of the slowD moving average (positive integers only). Default to 3
     * @param slowKMAType the type of moving average for the slowK moving average (0=SMA, 1=EMA, 2=WMA, 3=DEMA, 4=TEMA, 5=TRIMA, 6=T3, 7=KAMA, 8=MAMA). Default to 0
     * @param slowDMAType the type of moving average for the slowD moving average (0=SMA, 1=EMA, 2=WMA, 3=DEMA, 4=TEMA, 5=TRIMA, 6=T3, 7=KAMA, 8=MAMA). Default to 0
     * @return a list of STOCH values with dates
     */
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

    /**
     * Get Relative Strength Index (RSI) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod the time period of the RSI (positive integers only).
     * @param seriesType the desired price type in the time series (close, open, high, low).
     * @return a list of RSI values with dates
     */
    suspend fun getRSI(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int,
        seriesType: String
    ): TechnicalAnalysisHistory

    /**
     * Get Average Directional Movement Index (ADX) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod the time period of the ADX (positive integers only).
     * @return a list of ADX values with dates
     */
    suspend fun getADX(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    /**
     * Get Commodity Channel Index (CCI) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod number of data points used to calculate each CCI value (positive integers only).
     * @return a list of CCI values with dates
     */
    suspend fun getCCI(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    /**
     * Get Aroon (AROON) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod number of data points used to calculate each Aroon value (positive integers only).
     * @return a list of Aroon values (AroonUp, ArronDown) with dates
     */
    suspend fun getAROON(
        function: AnalysisType,
        symbol: String,
        interval: String,
        timePeriod: Int
    ): TechnicalAnalysisHistory

    /**
     * Get Bollinger Bands (BBANDS) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (1min, 5min, 15min, 30min, 60min, daily, weekly, monthly)
     * @param timePeriod number of data points used to calculate each Bollinger Bands value (positive integers only).
     * @param seriesType the desired price type in the time series (close, open, high, low).
     * @param nbDevUp the number of standard deviations above the middle band (positive integers only). Default to 2
     * @param nbDevDown the number of standard deviations below the middle band (positive integers only). Default to 2
     * @param matype the type of moving average to use (0=SMA, 1=EMA, 2=WMA, 3=DEMA, 4=TEMA, 5=TRIMA, 6=T3, 7=KAMA, 8=MAMA). Default to 0
     * @return a list of Bollinger Bands values (realUpperBand, realMiddleBand, realLowerBand) with dates
     */
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

    /**
     * Get Chaikin A/D Line (AD) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (daily, weekly, monthly)
     * @return a list of AD values with dates
     */
    suspend fun getAD(
        function: AnalysisType,
        symbol: String,
        interval: String
    ): TechnicalAnalysisHistory

    /**
     * Get On Balance Volume (OBV) values for a security
     * @param symbol the symbol of the security
     * @param interval the time interval between two consecutive data points in the time series (daily, weekly, monthly)
     * @return a list of OBV values with dates
     */
    suspend fun getOBV(
        function: AnalysisType,
        symbol: String,
        interval: String
    ): TechnicalAnalysisHistory


}