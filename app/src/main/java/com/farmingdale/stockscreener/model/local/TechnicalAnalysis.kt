package com.farmingdale.stockscreener.model.local

sealed class TechnicalAnalysis(
    open val date: String,
    open val value: String
)

data class SMA(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class EMA(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class STOCH(
    override val date: String,
    override val value: String,
    val slowK: String,
    val slowD: String
) : TechnicalAnalysis(date, value)

data class RSI(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class ADX(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class CCI(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class AROON(
    override val date: String,
    override val value: String,
    val aroonDown: String,
    val arronUp: String
) : TechnicalAnalysis(date, value)

data class BBANDS(
    override val date: String,
    override val value: String,
    val realUpperBand: String,
    val realMiddleBand: String,
    val realLowerBand: String
) : TechnicalAnalysis(date, value)

data class AD(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

data class OBV(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)