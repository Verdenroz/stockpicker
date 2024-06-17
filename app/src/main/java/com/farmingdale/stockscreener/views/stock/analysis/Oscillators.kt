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
fun OscillatorsSection(
    analysis: Analysis,
    signals: Map<AnalysisIndicators, String>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = stringResource(id = R.string.oscillators),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.25.sp,
            modifier = modifier.padding(start = 16.dp)
        )
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