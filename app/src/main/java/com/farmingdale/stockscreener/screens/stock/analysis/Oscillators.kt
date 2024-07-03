package com.farmingdale.stockscreener.screens.stock.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators

@Composable
fun OscillatorsSection(
    analysis: Analysis,
    signals: Map<AnalysisIndicators, String>,
) {
    Column {
        AnalysisDetail(
            title = stringResource(id = R.string.rsi14),
            signals = signals,
            value = analysis.rsi14,
            type = AnalysisIndicators.RSI
        )
        AnalysisDetail(
            title = stringResource(id = R.string.srsi14),
            signals = signals,
            value = analysis.srsi14,
            type = AnalysisIndicators.SRSI
        )
        AnalysisDetail(
            title = stringResource(id = R.string.stoch),
            signals = signals,
            value = analysis.stoch,
            type = AnalysisIndicators.STOCH
        )
        AnalysisDetail(
            title = stringResource(id = R.string.cci20),
            signals = signals,
            value = analysis.cci20,
            type = AnalysisIndicators.CCI
        )
    }
}