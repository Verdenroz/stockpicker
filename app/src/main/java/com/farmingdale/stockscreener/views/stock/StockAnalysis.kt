package com.farmingdale.stockscreener.views.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.Analysis
import com.farmingdale.stockscreener.model.local.indicators.Aroon
import com.farmingdale.stockscreener.model.local.indicators.BBands
import com.farmingdale.stockscreener.model.local.indicators.IchimokuCloud
import com.farmingdale.stockscreener.model.local.indicators.SuperTrend

@Composable
fun StockAnalysis(analysis: Analysis?) {
    if (analysis == null) {
        LinearProgressIndicator()
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnalysisDetail(
                title = stringResource(id = R.string.sma10),
                value = analysis.sma10
            )
            AnalysisDetail(
                title = stringResource(id = R.string.sma20),
                value = analysis.sma20
            )
            AnalysisDetail(
                title = stringResource(id = R.string.sma50),
                value = analysis.sma50
            )
        }
    }
}

@Composable
fun AnalysisDetail(title: String, value: Number) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                letterSpacing = 1.25.sp
            )
        },
        trailingContent = {
            Row {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    letterSpacing = 1.25.sp
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewStockAnalysis() {
    StockAnalysis(
        Analysis(
            sma10 = 1.0,
            sma20 = 2.0,
            sma50 = 3.0,
            sma100 = 3.5,
            sma200 = 4.0,
            ema10 = 1.0,
            ema20 = 2.0,
            ema50 = 3.0,
            ema100 = 3.5,
            ema200 = 4.0,
            wma10 = 1.0,
            wma20 = 2.0,
            wma50 = 3.0,
            wma100 = 3.5,
            wma200 = 4.0,
            vwma20 = 2.0,
            rsi14 = 5.0,
            srsi14 = 7.0,
            cci20 = 8.0,
            adx14 = 9.0,
            macd = 6.0,
            stoch = 10.0,
            obv = 11000,
            aroon = Aroon(11.0, 12.0),
            bBands = BBands(13.0, 14.0),
            superTrend = SuperTrend(16.0, "UP"),
            ichimokuCloud = IchimokuCloud(16.0, 17.0, 18.0, 19.0, 20.0)
        )
    )
}