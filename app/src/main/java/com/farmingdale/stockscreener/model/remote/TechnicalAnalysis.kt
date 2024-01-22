package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class TechnicalAnalysis(
    val analysisData: Map<String, AnalysisData>
)
