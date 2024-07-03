package com.farmingdale.stockscreener.screens.stock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.FullQuoteData
import com.farmingdale.stockscreener.model.local.MarketSector
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource

@Composable
fun StockPerformance(
    quote: FullQuoteData,
    sectorPerformance: Resource<MarketSector?, DataError.Network>
) {
    when (sectorPerformance) {
        is Resource.Loading -> {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }

        is Resource.Error -> {
            StockError(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        is Resource.Success -> {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.performance) + ": ${quote.symbol}",
                    style = MaterialTheme.typography.titleMedium,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(8.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                ) {
                    quote.ytdReturn?.let {
                        item(key = quote.ytdReturn) {
                            PerformanceCard(
                                label = stringResource(id = R.string.ytd_return),
                                symbol = quote.symbol,
                                sector = when (quote.sector) {
                                    "Technology" -> stringResource(id = R.string.sector_technology)
                                    "Healthcare" -> stringResource(id = R.string.sector_healthcare)
                                    "Financial Services" -> stringResource(id = R.string.sector_financial_services)
                                    "Consumer Cyclical" -> stringResource(id = R.string.sector_consumer_cyclical)
                                    "Industrials" -> stringResource(id = R.string.sector_industrials)
                                    "Consumer Defensive" -> stringResource(id = R.string.sector_consumer_defensive)
                                    "Energy" -> stringResource(id = R.string.sector_energy)
                                    "Real Estate" -> stringResource(id = R.string.sector_real_estate)
                                    "Utilities" -> stringResource(id = R.string.sector_utilities)
                                    "Basic Materials" -> stringResource(id = R.string.sector_basic_materials)
                                    "Communication Services" -> stringResource(id = R.string.sector_communication_services)
                                    else -> quote.sector
                                },
                                stockPerformance = it,
                                sectorPerformance = sectorPerformance.data?.ytdReturn
                            )
                        }
                    }
                    quote.yearReturn?.let {
                        item(key = quote.yearReturn) {
                            PerformanceCard(
                                label = stringResource(id = R.string.one_year_return),
                                symbol = quote.symbol,
                                sector = when (quote.sector) {
                                    "Technology" -> stringResource(id = R.string.sector_technology)
                                    "Healthcare" -> stringResource(id = R.string.sector_healthcare)
                                    "Financial Services" -> stringResource(id = R.string.sector_financial_services)
                                    "Consumer Cyclical" -> stringResource(id = R.string.sector_consumer_cyclical)
                                    "Industrials" -> stringResource(id = R.string.sector_industrials)
                                    "Consumer Defensive" -> stringResource(id = R.string.sector_consumer_defensive)
                                    "Energy" -> stringResource(id = R.string.sector_energy)
                                    "Real Estate" -> stringResource(id = R.string.sector_real_estate)
                                    "Utilities" -> stringResource(id = R.string.sector_utilities)
                                    "Basic Materials" -> stringResource(id = R.string.sector_basic_materials)
                                    "Communication Services" -> stringResource(id = R.string.sector_communication_services)
                                    else -> quote.sector
                                },
                                stockPerformance = it,
                                sectorPerformance = sectorPerformance.data?.yearReturn
                            )
                        }

                    }
                    quote.threeYearReturn?.let {
                        item(key = quote.threeYearReturn) {
                            PerformanceCard(
                                label = stringResource(id = R.string.three_year_return),
                                symbol = quote.symbol,
                                sector = when (quote.sector) {
                                    "Technology" -> stringResource(id = R.string.sector_technology)
                                    "Healthcare" -> stringResource(id = R.string.sector_healthcare)
                                    "Financial Services" -> stringResource(id = R.string.sector_financial_services)
                                    "Consumer Cyclical" -> stringResource(id = R.string.sector_consumer_cyclical)
                                    "Industrials" -> stringResource(id = R.string.sector_industrials)
                                    "Consumer Defensive" -> stringResource(id = R.string.sector_consumer_defensive)
                                    "Energy" -> stringResource(id = R.string.sector_energy)
                                    "Real Estate" -> stringResource(id = R.string.sector_real_estate)
                                    "Utilities" -> stringResource(id = R.string.sector_utilities)
                                    "Basic Materials" -> stringResource(id = R.string.sector_basic_materials)
                                    "Communication Services" -> stringResource(id = R.string.sector_communication_services)
                                    else -> quote.sector
                                },
                                stockPerformance = it,
                                sectorPerformance = sectorPerformance.data?.threeYearReturn
                            )
                        }

                    }
                    quote.fiveYearReturn?.let {
                        item(key = quote.fiveYearReturn) {
                            PerformanceCard(
                                label = stringResource(id = R.string.five_year_return),
                                symbol = quote.symbol,
                                sector = when (quote.sector) {
                                    "Technology" -> stringResource(id = R.string.sector_technology)
                                    "Healthcare" -> stringResource(id = R.string.sector_healthcare)
                                    "Financial Services" -> stringResource(id = R.string.sector_financial_services)
                                    "Consumer Cyclical" -> stringResource(id = R.string.sector_consumer_cyclical)
                                    "Industrials" -> stringResource(id = R.string.sector_industrials)
                                    "Consumer Defensive" -> stringResource(id = R.string.sector_consumer_defensive)
                                    "Energy" -> stringResource(id = R.string.sector_energy)
                                    "Real Estate" -> stringResource(id = R.string.sector_real_estate)
                                    "Utilities" -> stringResource(id = R.string.sector_utilities)
                                    "Basic Materials" -> stringResource(id = R.string.sector_basic_materials)
                                    "Communication Services" -> stringResource(id = R.string.sector_communication_services)
                                    else -> quote.sector
                                },
                                stockPerformance = it,
                                sectorPerformance = sectorPerformance.data?.fiveYearReturn
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PerformanceCard(
    label: String,
    symbol: String,
    sector: String?,
    stockPerformance: String,
    sectorPerformance: String?
) {
    Card(
        modifier = Modifier
            .size(250.dp, 125.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                letterSpacing = 1.5.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = symbol,
                        style = MaterialTheme.typography.titleSmall,
                        letterSpacing = 1.25.sp
                    )
                    Text(
                        text = stockPerformance,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (stockPerformance.contains('+')) positiveTextColor else negativeTextColor
                    )
                }
                if (sector != null && sectorPerformance != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = sector,
                            style = MaterialTheme.typography.titleSmall,
                            letterSpacing = 1.25.sp
                        )
                        Text(
                            text = sectorPerformance,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (sectorPerformance.contains('+')) positiveTextColor else negativeTextColor
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPerformanceCard() {
    PerformanceCard(
        label = "YTD Return",
        symbol = "AAPL",
        sector = "Technology",
        stockPerformance = "+10.00%",
        sectorPerformance = "+5.00%"
    )
}