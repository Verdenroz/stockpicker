package com.farmingdale.stockscreener.views.stock.analysis

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.model.local.indicators.Aroon
import com.farmingdale.stockscreener.model.local.indicators.BBands
import com.farmingdale.stockscreener.model.local.indicators.IchimokuCloud
import com.farmingdale.stockscreener.model.local.indicators.Macd
import com.farmingdale.stockscreener.model.local.indicators.SuperTrend
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockAnalysis(
    analysis: Analysis?,
    signals: Map<AnalysisIndicators, String>,
    movingAverageSummary: Double,
    oscillatorSummary: Double,
    trendSummary: Double,
    overallSummary: Double
) {
    if (analysis == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item(key = "summary") {
                    SummaryAnalysisSection(
                        movingAverageSummary = movingAverageSummary,
                        oscillatorsSummary = oscillatorSummary,
                        trendsSummary = trendSummary,
                        overallSummary = overallSummary,
                        signals = signals,
                        listState = listState
                    )
                }
                stickyHeader {
                    Text(
                        text = stringResource(id = R.string.moving_averages),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    )
                }
                item(key = "moving_average") {
                    MovingAverageSection(
                        analysis = analysis,
                        signals = signals
                    )
                }
                stickyHeader {
                    Text(
                        text = stringResource(id = R.string.oscillators),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    )
                }
                item(key = "oscillators") {
                    OscillatorsSection(
                        analysis = analysis,
                        signals = signals
                    )
                }
                stickyHeader {
                    Text(
                        text = stringResource(id = R.string.trends),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    )
                }
                item(key = "trends") {
                    TrendsSection(
                        analysis = analysis,
                        signals = signals,
                    )
                }
            }
        }
    }
}


@Composable
fun AnalysisDetail(
    signals: Map<AnalysisIndicators, String>,
    title: String,
    value: Double,
    type: AnalysisIndicators
) {
    val signal = signals[type] ?: ""
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                letterSpacing = 1.25.sp,
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(.35f)
            ) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 1.25.sp
                )
                Text(
                    text = signal,
                    style = MaterialTheme.typography.titleSmall,
                    letterSpacing = if (signal.length <= 4) 1.25.sp else 1.sp,
                    color = when (signal) {
                        LocalContext.current.getString(R.string.buy) -> positiveTextColor
                        LocalContext.current.getString(R.string.sell) -> negativeTextColor
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontSize = when (signal) {
                        LocalContext.current.getString(R.string.very_strong_trend) -> 13.sp
                        LocalContext.current.getString(R.string.extreme_trend) -> 12.sp
                        else -> 14.sp
                    }
                )
            }
        }
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.inverseOnSurface,
        thickness = Dp.Hairline
    )
}


@Preview
@Composable
fun PreviewStockAnalysis() {
    Surface {
        StockAnalysis(
            Analysis(
                sma10 = 100.0,
                sma20 = 200.0,
                sma50 = 300.0,
                sma100 = 333.5,
                sma200 = 444.0,
                ema10 = 100.0,
                ema20 = 200.0,
                ema50 = 250.0,
                ema100 = 333.5,
                ema200 = 400.0,
                wma10 = 1.0,
                wma20 = 2.0,
                wma50 = 3.0,
                wma100 = 3.5,
                wma200 = 4.0,
                vwma20 = 2.0,
                rsi14 = 55.0,
                srsi14 = 7.0,
                cci20 = 8.0,
                adx14 = 9.0,
                macd = Macd(10.0, 11.0),
                stoch = 10.0,
                aroon = Aroon(11.0, 12.0),
                bBands = BBands(13.0, 14.0),
                superTrend = SuperTrend(16.0, "UP"),
                ichimokuCloud = IchimokuCloud(16.0, 17.0, 18.0, 19.0, 20.0)
            ),
            signals = mapOf(
                AnalysisIndicators.SMA10 to "Buy",
                AnalysisIndicators.SMA50 to "Buy",
                AnalysisIndicators.RSI to "Sell",
                AnalysisIndicators.ADX to "Buy"
            ),
            movingAverageSummary = 0.0,
            oscillatorSummary = 0.0,
            trendSummary = 0.0,
            overallSummary = 0.0
        )
    }
}