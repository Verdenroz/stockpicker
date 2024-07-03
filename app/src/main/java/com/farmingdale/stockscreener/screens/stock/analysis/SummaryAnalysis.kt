package com.farmingdale.stockscreener.screens.stock.analysis

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.SummaryBar(
    title: String,
    value: Double,
    actions: Triple<Int, Int, Int>,
    scrollTo: () -> Unit = {}
) {
    val epsilon = 0.001f // small constant to ensure weight is always > 0
    val (sell, neutral, buy) = actions
    val suggestion = when {
        buy > sell && buy > neutral -> stringResource(id = R.string.buy)
        sell > buy && sell > neutral -> stringResource(id = R.string.sell)
        else -> stringResource(id = R.string.neutral)
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { scrollTo() }
            .weight(.5f),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            letterSpacing = 1.25.sp,
            fontWeight = FontWeight.Black
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = suggestion,
                style = MaterialTheme.typography.titleMedium,
                color = when (suggestion) {
                    stringResource(id = R.string.buy) -> positiveTextColor
                    stringResource(id = R.string.sell) -> negativeTextColor
                    else -> MaterialTheme.colorScheme.primary
                },
                letterSpacing = 1.25.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryCountCell(title = stringResource(id = R.string.sell), value = sell)
                SummaryCountCell(title = stringResource(id = R.string.neutral), value = neutral)
                SummaryCountCell(title = stringResource(id = R.string.buy), value = buy)
            }
        }
    }
}

@Composable
fun SummaryCountCell(
    title: String,
    value: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SummaryAnalysisSection(
    movingAverageSummary: Double,
    oscillatorsSummary: Double,
    trendsSummary: Double,
    overallSummary: Double,
    signals: Map<AnalysisIndicators, String>,
    listState: LazyListState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val overallSignalsCount = remember(signals) { countSignals(signals, context) }
    val trendsSignalsCount = remember(signals) {
        countSignals(
            signals.filterKeys { it in AnalysisIndicators.TRENDS },
            context
        )
    }
    val movingAveragesSignalsCount = remember(signals) {
        countSignals(
            signals.filterKeys { it in AnalysisIndicators.MOVING_AVERAGES },
            context
        )
    }
    val oscillatorsSignalsCount = remember(signals) {
        countSignals(
            signals.filterKeys { it in AnalysisIndicators.OSCILLATORS },
            context
        )
    }

    FlowRow(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        maxItemsInEachRow = 2,
        verticalArrangement = Arrangement.Center
    ) {
        SummaryBar(
            title = stringResource(id = R.string.overall),
            value = overallSummary,
            actions = overallSignalsCount,
        )
        SummaryBar(
            title = stringResource(id = R.string.trends),
            value = trendsSummary,
            actions = trendsSignalsCount,
            scrollTo = {
                scope.launch {
                    listState.animateScrollToItem(15)
                }
            }
        )
        SummaryBar(
            title = stringResource(id = R.string.moving_averages),
            value = movingAverageSummary,
            actions = movingAveragesSignalsCount,
            scrollTo = {
                scope.launch {
                    listState.animateScrollToItem(1)
                }
            }
        )
        SummaryBar(
            title = stringResource(id = R.string.oscillators),
            value = oscillatorsSummary,
            actions = oscillatorsSignalsCount,
            scrollTo = {
                scope.launch {
                    listState.animateScrollToItem(5)
                }
            }
        )
    }
}

private fun countSignals(
    signals: Map<AnalysisIndicators, String>,
    context: Context
): Triple<Int, Int, Int> {
    var positive = 0
    var negative = 0
    var neutral = 0
    signals.forEach {
        when (it.value) {
            context.getString(R.string.buy),
            context.getString(R.string.strong_trend),
            context.getString(R.string.very_strong_trend),
            context.getString(R.string.extreme_trend) -> positive++

            context.getString(R.string.sell),
            context.getString(R.string.weak_trend) -> negative++

            else -> neutral++
        }
    }
    return Triple(negative, neutral, positive)
}