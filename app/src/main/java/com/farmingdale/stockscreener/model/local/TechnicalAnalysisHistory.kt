package com.farmingdale.stockscreener.model.local

/**
 * Contains the history of [TechnicalAnalysis] ranging from most recent first to oldest last for a stock
 * @param analyses the list of [TechnicalAnalysis] for a stock
 */
data class TechnicalAnalysisHistory(
    val analyses: List<TechnicalAnalysis>
)
