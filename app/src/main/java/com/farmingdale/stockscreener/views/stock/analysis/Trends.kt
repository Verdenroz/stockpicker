package com.farmingdale.stockscreener.views.stock.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators

@Composable
fun TrendsSection(
    analysis: Analysis,
    signals: Map<AnalysisIndicators, String>,
) {
    Column {
        AnalysisDetail(
            title = stringResource(id = R.string.adx14),
            signals = signals,
            value = analysis.adx14,
            type = AnalysisIndicators.ADX
        )
        AnalysisDetail(
            title = stringResource(id = R.string.macd),
            signals = signals,
            value = analysis.macd.macd,
            type = AnalysisIndicators.MACD
        )
        AnalysisDetail(
            title = stringResource(id = R.string.bbands),
            signals = signals,
            value = analysis.bBands.upperBand,
            type = AnalysisIndicators.BBANDS
        )
        AnalysisDetail(
            title = stringResource(id = R.string.aroon),
            signals = signals,
            value = analysis.aroon.aroonUp,
            type = AnalysisIndicators.AROON
        )
        AnalysisDetail(
            title = stringResource(id = R.string.super_trend),
            signals = signals,
            value = analysis.superTrend.superTrend,
            type = AnalysisIndicators.SUPERTREND
        )
        if (analysis.ichimokuCloud.leadingSpanA != null && analysis.ichimokuCloud.leadingSpanB != null) {
            AnalysisDetail(
                title = stringResource(id = R.string.ichimoku),
                signals = signals,
                value = analysis.ichimokuCloud.leadingSpanA,
                type = AnalysisIndicators.ICHIMOKUCLOUD
            )
        }
    }
}