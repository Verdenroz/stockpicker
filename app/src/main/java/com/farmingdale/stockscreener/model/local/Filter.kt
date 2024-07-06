package com.farmingdale.stockscreener.model.local

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

/**
 * Filter for region of stock exchanges
 */
@Stable
enum class RegionFilter(val exchanges: ImmutableSet<String>) {
    US(persistentSetOf("NASDAQ", "NYSE", "AMEX", "ETF", "CBOE")),
    NA(
        persistentSetOf(
            "NASDAQ",
            "NYSE",
            "AMEX",
            "ETF",
            "PNK",
            "OTC",
            "PNK",
            "OTC",
            "CBOE",
            "NEO",
            "TSX",
            "TSXV",
            "CNQ",
            "MEX"
        )
    ),
    SA(persistentSetOf("SAO", "BUE", "SGO")),
    EU(
        persistentSetOf(
            "LSE",
            "AQS",
            "XETRA",
            "STU",
            "BER",
            "DUS",
            "MUN",
            "HAM",
            "BME",
            "PRA",
            "EURONEXT",
            "PAR",
            "BRU",
            "STO",
            "ICE",
            "CPH",
            "HEL",
            "RIS",
            "MIL",
            "WSE",
            "SIX",
            "OSL",
            "VIE",
            "SES",
            "BUD",
            "AMS",
            "ATH",
            "IST",
            "DXE",
            "IOB",
        )
    ),
    AS(
        persistentSetOf(
            "BSE",
            "NSE",
            "JPX",
            "SHZ",
            "SHH",
            "HKSE",
            "KSC",
            "KLS",
            "KOE",
            "TAI",
            "TWO",
            "SET",
            "JKT",
            "CAI"
        )
    ),
    AF(persistentSetOf("JNB", "EGY", "CAI")),
    AU(persistentSetOf("ASX", "NZE")),
    ME(persistentSetOf("TLV", "SAU", "DOH", "DFM", "KUW")),
    GLOBAL(
        (NA.exchanges + SA.exchanges + EU.exchanges + AS.exchanges + AF.exchanges + AU.exchanges + ME.exchanges).toImmutableSet()
    )
}

/**
 * Filter for type of stock
 */
@Stable
enum class TypeFilter(val type: String) {
    STOCK("stock"),
    ETF("etf"),
    TRUST("trust"),
}
