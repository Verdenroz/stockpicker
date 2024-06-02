package com.farmingdale.stockscreener.model.local

/**
 * Filter for region of stock exchanges
 */
enum class RegionFilter(val exchanges: List<String>) {
    US(listOf("NASDAQ", "NYSE", "AMEX", "ETF", "CBOE")),
    NA(
        listOf(
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
    SA(listOf("SAO", "BUE", "SGO")),
    EU(
        listOf(
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
        listOf(
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
    AF(listOf("JNB", "EGY", "CAI")),
    AU(listOf("ASX", "NZE")),
    ME(listOf("TLV", "SAU", "DOH", "DFM", "KUW")),
    GLOBAL(
        NA.exchanges + SA.exchanges + EU.exchanges + AS.exchanges + AF.exchanges + AU.exchanges + ME.exchanges
    )
}

/**
 * Filter for type of stock
 */
enum class TypeFilter(val type: String) {
    STOCK("stock"),
    ETF("etf"),
    TRUST("trust"),
}
