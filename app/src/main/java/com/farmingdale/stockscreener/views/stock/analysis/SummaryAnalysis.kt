package com.farmingdale.stockscreener.views.stock.analysis

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor

@Composable
fun SummaryBar(title: String, value: Double) {
    val epsilon = 0.001f // small constant to ensure weight is always > 0
    Text(
        text = title,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(((1 - value) / 2).toFloat() + epsilon)
                .fillMaxHeight()
                .background(negativeBackgroundColor)
        )
        Box(
            modifier = Modifier
                .weight(((1 + value) / 2).toFloat() + epsilon)
                .fillMaxHeight()
                .background(positiveBackgroundColor)
        )
    }
}

@Composable
fun SummaryAnalysisSection(
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double
) {
    Log.d("SummaryAnalysisSection", "movingAverageSummary: $movingAverageSummary")
    Log.d("SummaryAnalysisSection", "oscillatorsSummary: $oscillatorsSummary")
    Log.d("SummaryAnalysisSection", "trendsSummary: $trendsSummary")
    Log.d("SummaryAnalysisSection", "overallSummary: $overallSummary")
    SummaryBar(title = "Overall Summary", value = overallSummary)
    SummaryBar(title = "Moving Average Summary", value = movingAverageSummary)
    SummaryBar(title = "Oscillators Summary", value = oscillatorsSummary)
    SummaryBar(title = "Trends Summary", value = trendsSummary)
}