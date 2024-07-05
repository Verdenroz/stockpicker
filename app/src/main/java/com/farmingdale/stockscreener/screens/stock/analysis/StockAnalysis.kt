package com.farmingdale.stockscreener.screens.stock.analysis

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.indicators.AnalysisIndicators
import com.farmingdale.stockscreener.model.local.indicators.Aroon
import com.farmingdale.stockscreener.model.local.indicators.BBands
import com.farmingdale.stockscreener.model.local.indicators.IchimokuCloud
import com.farmingdale.stockscreener.model.local.indicators.Macd
import com.farmingdale.stockscreener.model.local.indicators.SuperTrend
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StockAnalysis(
    symbol: String,
    snackbarHost: SnackbarHostState,
    analysis: Resource<Analysis?, DataError.Network>,
    signals: ImmutableMap<AnalysisIndicators, String>,
    movingAverageSummary: Double,
    oscillatorSummary: Double,
    trendSummary: Double,
    overallSummary: Double,
    updateAnalysis: (String, Interval) -> Unit,
) {
    val context = LocalContext.current
    when (analysis) {
        is Resource.Loading -> {
            StockAnalysisSkeleton()
        }

        is Resource.Error -> {
            StockAnalysisSkeleton()

            LaunchedEffect(analysis.error) {
                snackbarHost.showSnackbar(
                    message = analysis.error.asUiText().asString(context),
                    actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }

        is Resource.Success -> {
            var selectedInterval by rememberSaveable { mutableStateOf(Interval.DAILY) }
            if (analysis.data == null) {
                Column(
                    modifier = Modifier
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnalysisIntervalBar(
                        modifier = Modifier.fillMaxWidth(),
                        symbol = symbol,
                        selectedInterval = selectedInterval,
                        updateInterval = { selectedInterval = it },
                        updateAnalysis = updateAnalysis,
                    )
                    Text(
                        text = stringResource(id = R.string.no_analysis),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 64.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(100))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnalysisIntervalBar(
                        modifier = Modifier.fillMaxWidth(),
                        symbol = symbol,
                        selectedInterval = selectedInterval,
                        updateInterval = { selectedInterval = it },
                        updateAnalysis = updateAnalysis,
                    )
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
                                analysis = analysis.data,
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
                                analysis = analysis.data,
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
                                analysis = analysis.data,
                                signals = signals,
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AnalysisDetail(
    signals: ImmutableMap<AnalysisIndicators, String>,
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

@Composable
fun AnalysisIntervalBar(
    modifier: Modifier = Modifier,
    symbol: String,
    selectedInterval: Interval,
    updateInterval: (Interval) -> Unit,
    updateAnalysis: (String, Interval) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Interval.entries.filter { interval ->
            interval in setOf(
                Interval.FIFTEEN_MINUTE,
                Interval.THIRTY_MINUTE,
                Interval.ONE_HOUR,
                Interval.DAILY,
                Interval.WEEKLY,
                Interval.MONTHLY
            )
        }.forEach { interval ->
            IntervalButton(
                interval = interval,
                selectedInterval = interval == selectedInterval,
                onClick = {
                    updateInterval(interval)
                    updateAnalysis(symbol, interval)
                }
            )
        }
    }
}

@Composable
fun IntervalButton(
    interval: Interval,
    selectedInterval: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioButton(selected = selectedInterval, onClick = onClick)
        Text(
            text = interval.value.uppercase(),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
fun PreviewStockAnalysis() {
    Surface {
        StockAnalysis(
            symbol = "AAPL",
            snackbarHost = SnackbarHostState(),
            analysis = Resource.Success(
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
                )
            ),
            signals = mapOf(
                AnalysisIndicators.SMA10 to "Buy",
                AnalysisIndicators.SMA50 to "Buy",
                AnalysisIndicators.RSI to "Sell",
                AnalysisIndicators.ADX to "Buy"
            ).toImmutableMap(),
            movingAverageSummary = 0.0,
            oscillatorSummary = 0.0,
            trendSummary = 0.0,
            overallSummary = 0.0,
            updateAnalysis = { _, _ -> },
        )
    }
}

@Composable
fun StockAnalysisSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    Card(
        modifier = modifier
            .height(300.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        // skeleton
    }
}