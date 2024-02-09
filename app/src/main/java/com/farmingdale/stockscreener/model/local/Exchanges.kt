package com.farmingdale.stockscreener.model.local

/**
 * Supported exchanges in FinancialModelPrep
 */
sealed interface Exchange
/**
 * Supported exchanges in FinancialModelPrep for NA
 */
enum class NorthAmericanExchanges: Exchange {
    NYSE,
    NASDAQ,
    AMEX,
    OTC,
    CBOE,
    ASE,
    TSX,
    TSXV,
    CNQ,
    NEO,
    MEX,
}

/**
 * Supported exchanges in FinancialModelPrep for SA
 */
enum class SouthAmericanExchanges: Exchange {
    SAO,
    SGO,
}

/**
 * Supported exchanges in FinancialModelPrep for EU
 */
enum class EuropeanExchanges: Exchange {
    EURONEXT,
    XETRA,
    MCX,
    LSE,
    BME,
    SIX,
    ICE,
    MIL,
    ISE,
    CPH,
    HEL,
    STO,
    WSE,
    OSL,
    JNB,
    ATH,
    PRA,
    BRU
}
/**
 * Supported exchanges in FinancialModelPrep for Asia and Middle East
 */
enum class AsianExchanges: Exchange {
    NSE,
    BSE,
    JKT,
    SET,
    SES,
    SHZ,
    SHH,
    HKSE,
    TWO,
    TAI,
    KLS,
    JPX,
    KOE,
    KSC,
    TLV,
    SAU,
    PNK,
}

/**
 * Supported exchanges in FinancialModelPrep for AU
 */
enum class AustralianExchanges: Exchange {
    ASX,
}