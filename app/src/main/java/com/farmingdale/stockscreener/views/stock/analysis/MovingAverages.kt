package com.farmingdale.stockscreener.views.stock.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators

@Composable
fun MovingAverageSection(
    analysis: Analysis,
    signals: Map<AnalysisIndicators, String>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = stringResource(id = R.string.moving_averages),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.25.sp,
            modifier = modifier.padding(start = 16.dp)
        )
        AnalysisDetail(
            title = stringResource(id = R.string.sma10),
            signals = signals,
            value = analysis.sma10,
            type = AnalysisIndicators.SMA10
        )
        AnalysisDetail(
            title = stringResource(id = R.string.sma20),
            signals = signals,
            value = analysis.sma20,
            type = AnalysisIndicators.SMA20
        )
        AnalysisDetail(
            title = stringResource(id = R.string.sma50),
            signals = signals,
            value = analysis.sma50,
            type = AnalysisIndicators.SMA50
        )
        AnalysisDetail(
            title = stringResource(id = R.string.sma100),
            signals = signals,
            value = analysis.sma100,
            type = AnalysisIndicators.SMA100
        )
        AnalysisDetail(
            title = stringResource(id = R.string.sma200),
            signals = signals,
            value = analysis.sma200,
            type = AnalysisIndicators.SMA200
        )
        AnalysisDetail(
            title = stringResource(id = R.string.ema10),
            signals = signals,
            value = analysis.ema10,
            type = AnalysisIndicators.EMA10
        )
        AnalysisDetail(
            title = stringResource(id = R.string.ema20),
            signals = signals,
            value = analysis.ema20,
            type = AnalysisIndicators.EMA20
        )
        AnalysisDetail(
            title = stringResource(id = R.string.ema50),
            signals = signals,
            value = analysis.ema50,
            type = AnalysisIndicators.EMA50
        )
        AnalysisDetail(
            title = stringResource(id = R.string.ema100),
            signals = signals,
            value = analysis.ema100,
            type = AnalysisIndicators.EMA100
        )
        AnalysisDetail(
            title = stringResource(id = R.string.ema200),
            signals = signals,
            value = analysis.ema200,
            type = AnalysisIndicators.EMA200
        )
        AnalysisDetail(
            title = stringResource(id = R.string.wma10),
            signals = signals,
            value = analysis.wma10,
            type = AnalysisIndicators.WMA20
        )
        AnalysisDetail(
            title = stringResource(id = R.string.wma20),
            signals = signals,
            value = analysis.wma20,
            type = AnalysisIndicators.WMA20
        )
        AnalysisDetail(
            title = stringResource(id = R.string.wma50),
            signals = signals,
            value = analysis.wma50,
            type = AnalysisIndicators.WMA50
        )
        AnalysisDetail(
            title = stringResource(id = R.string.wma100),
            signals = signals,
            value = analysis.wma100,
            type = AnalysisIndicators.WMA100
        )
        AnalysisDetail(
            title = stringResource(id = R.string.wma200),
            signals = signals,
            value = analysis.wma200,
            type = AnalysisIndicators.WMA200
        )
        AnalysisDetail(
            title = stringResource(id = R.string.vwma20),
            signals = signals,
            value = analysis.vwma20,
            type = AnalysisIndicators.VWMA20
        )
    }
}