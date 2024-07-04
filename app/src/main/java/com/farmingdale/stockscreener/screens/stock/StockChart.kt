package com.farmingdale.stockscreener.screens.stock

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmingdale.stockscreener.R
import com.farmingdale.stockscreener.model.local.HistoricalData
import com.farmingdale.stockscreener.model.local.Interval
import com.farmingdale.stockscreener.model.local.TimePeriod
import com.farmingdale.stockscreener.ui.theme.negativeBackgroundColor
import com.farmingdale.stockscreener.ui.theme.negativeTextColor
import com.farmingdale.stockscreener.ui.theme.positiveBackgroundColor
import com.farmingdale.stockscreener.ui.theme.positiveTextColor
import com.farmingdale.stockscreener.utils.DataError
import com.farmingdale.stockscreener.utils.Resource
import com.farmingdale.stockscreener.utils.UiText
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private const val SPACING = 75f

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    snackbarHost: SnackbarHostState,
    symbol: String,
    timeSeries: Resource<ImmutableMap<String, HistoricalData>, DataError.Network>,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    updateTimeSeries: (String, TimePeriod, Interval) -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    val context = LocalContext.current
    when (timeSeries) {
        is Resource.Loading -> {
            StockChartSkeleton()
        }

        is Resource.Error -> {
            StockChartSkeleton()

            LaunchedEffect(timeSeries.error) {
                snackbarHost.showSnackbar(
                    message = timeSeries.error.asUiText().asString(context),
                    actionLabel = UiText.StringResource(R.string.dismiss).asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }

        is Resource.Success -> {
            val density = LocalDensity.current
            val scope = rememberCoroutineScope()

            val data = timeSeries.data.entries.toList()
                .asReversed()
                .associate { it.key to it.value }

            val guidelineColor = MaterialTheme.colorScheme.outline

            val positiveChart = remember(data) {
                timeSeries.data.values.first().close > timeSeries.data.values.last().close
            }

            val upperValue = remember(data) {
                data.values.maxOf { it.close }.plus(1)
            }
            val lowerValue = remember(data) {
                data.values.minOf { it.close }.minus(1)
            }

            val selectedData = rememberSaveable {
                mutableStateOf<Pair<String, HistoricalData>?>(null)
            }

            Text(
                text = selectedData.value.let {
                    it?.let { (_, historicalData) ->
                        "${historicalData.close}"
                    } ?: ""
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = selectedData.value.let {
                        it?.let { (date, _) ->
                            date
                        } ?: ""
                    },
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 1.25.sp,
                )
                Text(
                    text = selectedData.value.let {
                        it?.let { (_, historicalData) ->
                            stringResource(R.string.volume) + " ${formatVolume(historicalData.volume)}"
                        } ?: ""
                    },
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 1.25.sp,
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                    .background(backgroundColor)
            ) {
                Box(modifier = modifier
                    .pointerInput(data) {
                        detectTapGestures(
                            onPress = { offset ->
                                val spacePerHour = (size.width - SPACING) / data.size
                                val index = ((offset.x - SPACING) / spacePerHour).toInt()

                                if (index in data.entries.indices) {
                                    selectedData.value = data.entries
                                        .elementAt(index)
                                        .toPair()
                                } else {
                                    selectedData.value = null
                                }
                                scope.launch { listState.animateScrollToItem(0) }
                            },
                            onDoubleTap = {
                                selectedData.value = null
                            },
                            onTap = {
                                selectedData.value = null
                            }
                        )
                    }
                    .pointerInput(data) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                val spacePerHour = (size.width - SPACING) / data.size
                                // Calculate the index of the selected data based on the x-coordinate of the drag
                                val index =
                                    ((change.position.x - SPACING) / spacePerHour).toInt()
                                if (index in data.entries.indices
                                ) {
                                    // Update the selected data
                                    selectedData.value = data.entries
                                        .elementAt(index)
                                        .toPair()
                                } else {
                                    // Clear the selected data if the drag is outside the data range
                                    selectedData.value = null
                                }
                                scope.launch { listState.animateScrollToItem(0) }
                            },
                            onDragEnd = {
                                // Clear the selected data when the drag ends
                                selectedData.value = null
                            }
                        )
                    }
                    .drawWithCache {
                        val topShader =
                            if (positiveChart) positiveTextColor.copy(alpha = .5f) else negativeTextColor.copy(
                                alpha = .5f
                            )
                        val bottomShader =
                            if (positiveChart) positiveBackgroundColor else negativeBackgroundColor
                        val textColor =
                            if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                        val textPaint = Paint().apply {
                            color = textColor
                            textAlign = Paint.Align.CENTER
                            textSize = density.run { 12.sp.toPx() }
                            typeface = android.graphics.Typeface.DEFAULT_BOLD
                        }
                        onDrawBehind {
                            drawChart(
                                timeSeries = data,
                                size = size,
                                lowerValue = lowerValue,
                                upperValue = upperValue,
                                topShader = topShader,
                                bottomShader = bottomShader,
                                guidelineColor = guidelineColor,
                                textPaint = textPaint,
                                density = density,
                                selectedData = selectedData
                            )
                        }
                    }
                )

                TimePeriodBar(
                    modifier = Modifier.fillMaxWidth(),
                    symbol = symbol,
                    updateTimeSeries = updateTimeSeries
                )
            }
        }
    }
}

@Composable
fun TimePeriodBar(
    modifier: Modifier = Modifier,
    symbol: String,
    updateTimeSeries: (String, TimePeriod, Interval) -> Unit
) {
    var selectedTimePeriod by rememberSaveable { mutableStateOf(TimePeriod.YEAR_TO_DATE) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimePeriod.entries.filter { timePeriod ->
            timePeriod in listOf(
                TimePeriod.ONE_DAY,
                TimePeriod.FIVE_DAY,
                TimePeriod.ONE_MONTH,
                TimePeriod.SIX_MONTH,
                TimePeriod.ONE_YEAR,
                TimePeriod.YEAR_TO_DATE,
                TimePeriod.FIVE_YEAR
            )
        }.forEach { timePeriod ->
            val interval =
                if (timePeriod == TimePeriod.ONE_DAY || timePeriod == TimePeriod.FIVE_DAY) Interval.FIFTEEN_MINUTE else Interval.DAILY
            TimePeriodButton(
                timePeriod = timePeriod,
                selected = timePeriod == selectedTimePeriod,
                onClick = {
                    selectedTimePeriod = timePeriod
                    updateTimeSeries(symbol, timePeriod, interval)
                }
            )
        }
    }
}

@Composable
fun TimePeriodButton(
    timePeriod: TimePeriod,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = timePeriod.value.uppercase(),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun StockChartSkeleton(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust the height as needed
            .padding(16.dp)
            .background(color)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw the horizontal axis
            drawLine(
                start = Offset(x = 0f, y = canvasHeight - 20f),
                end = Offset(x = canvasWidth, y = canvasHeight - 20f),
                color = Color.LightGray,
                strokeWidth = 4f
            )

            // Draw the vertical axis
            drawLine(
                start = Offset(x = 20f, y = 0f),
                end = Offset(x = 20f, y = canvasHeight),
                color = Color.LightGray,
                strokeWidth = 4f
            )

            // Optionally, draw some lines or dots to mimic data points
            val step = canvasWidth / 10
            for (i in 1..9) {
                drawCircle(
                    center = Offset(x = step * i, y = canvasHeight / 2),
                    radius = 4f,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Helper function to draw the chart given [timeSeries]
 */
fun DrawScope.drawChart(
    timeSeries: Map<String, HistoricalData>,
    size: Size,
    lowerValue: Float,
    upperValue: Float,
    topShader: Color,
    bottomShader: Color,
    guidelineColor: Color,
    textPaint: Paint,
    density: Density,
    selectedData: MutableState<Pair<String, HistoricalData>?>
) {
    val spacePerHour = (size.width - SPACING) / timeSeries.size

    // Draw the y-axis labels
    drawYAxis(
        drawScope = this,
        textPaint = textPaint,
        size = size,
        lowerValue = lowerValue,
        upperValue = upperValue
    )

    // Draw the x-axis labels
    drawXAxis(
        drawScope = this,
        textPaint = textPaint,
        size = size,
        earliestDate = timeSeries.keys.first(),
        spacePerHour = spacePerHour,
        data = timeSeries
    )
    // Draw the stroke and fill path
    drawPaths(
        drawScope = this,
        data = timeSeries,
        size = size,
        lowerValue = lowerValue,
        upperValue = upperValue,
        spacePerHour = spacePerHour,
        topShader = topShader,
        bottomShader = bottomShader,
        density = density
    )

    // Draw the selected data
    drawSelectedData(
        drawScope = this,
        selectedData = selectedData.value,
        data = timeSeries,
        size = size,
        lowerValue = lowerValue,
        upperValue = upperValue,
        spacePerHour = spacePerHour,
        guidelineColor = guidelineColor,
        density = density
    )
}

/**
 * Helper function to draw the y-axis labels
 */
fun drawYAxis(
    drawScope: DrawScope,
    textPaint: Paint,
    size: Size,
    lowerValue: Float,
    upperValue: Float
) {
    val priceStep = (upperValue - lowerValue) / 5f
    (0..4).forEach { i ->
        drawScope.drawContext.canvas.nativeCanvas.apply {
            val price = round(lowerValue + priceStep * i).toString()
            val yPos = size.height - SPACING - i * size.height / 5f

            drawText(price, 5f, yPos - SPACING / 2 + 50f, textPaint)
        }
    }
}

/**
 * Helper function to draw the x-axis labels and line
 */
fun drawXAxis(
    drawScope: DrawScope,
    textPaint: Paint,
    size: Size,
    earliestDate: String,
    spacePerHour: Float,
    data: Map<String, HistoricalData>
) {
    val stepSize = getStepSize(earliestDate)
    for (i in data.entries.indices step stepSize) {
        val date = data.entries.elementAt(i).key
        val formattedDate = formatDate(date, earliestDate)
        val x = SPACING + i * spacePerHour

        drawScope.drawContext.canvas.nativeCanvas.drawText(
            formattedDate,
            x + 10f,
            size.height + 5f,
            textPaint
        )

        // Draw the tick above the label
        drawScope.drawLine(
            color = Color(textPaint.color),
            start = Offset(x, size.height - SPACING),
            end = Offset(x, size.height - 50f),
            strokeWidth = 5f
        )

        // Draw the x-axis line
        drawScope.drawLine(
            color = Color(textPaint.color),
            start = Offset(SPACING, size.height - SPACING),
            end = Offset(
                size.width,
                size.height - SPACING
            ),
            strokeWidth = 2f
        )
    }
}

/**
 * Helper function to draw the stroke and fill path
 */
fun drawPaths(
    drawScope: DrawScope,
    data: Map<String, HistoricalData>,
    size: Size,
    lowerValue: Float,
    upperValue: Float,
    spacePerHour: Float,
    topShader: Color,
    bottomShader: Color,
    density: Density
) {
    var lastX = 0f

    val strokePath = Path().apply {
        val height = size.height
        for (i in data.entries.indices) {
            val info = data.entries.elementAt(i).value
            val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)

            val x = SPACING + i * spacePerHour
            val y = height - SPACING - (leftRatio * height)
            if (i == 0) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
            if (i == data.entries.size - 1) {
                lastX = x
            }
        }
    }

    val fillPath = Path().apply {
        addPath(strokePath)
        lineTo(lastX, size.height - SPACING)
        lineTo(SPACING, size.height - SPACING)
        close()
    }

    // Draw the fill path
    drawScope.drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                bottomShader,
                Color.Transparent
            ),
            endY = size.height - SPACING
        )
    )

    // Draw the stroke path
    drawScope.drawPath(
        path = strokePath,
        color = topShader,
        style = Stroke(
            width = with(density) { 3.dp.toPx() },
            cap = StrokeCap.Round
        )
    )
}

/**
 * Helper function to draw the selected data with guideline/indicator on drag or tap
 */
fun drawSelectedData(
    drawScope: DrawScope,
    selectedData: Pair<String, HistoricalData>?,
    data: Map<String, HistoricalData>,
    size: Size,
    lowerValue: Float,
    upperValue: Float,
    spacePerHour: Float,
    guidelineColor: Color,
    density: Density
) {
    // Draw the selected data
    selectedData?.let { (date, historicalData) ->
        val index = data.entries.indexOfFirst { it.key == date }
        val x = SPACING + index * spacePerHour
        val ratio = (historicalData.close - lowerValue) / (upperValue - lowerValue)
        val y = size.height - SPACING - (ratio * size.height)

        // Draw the guideline
        drawScope.drawLine(
            color = guidelineColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height - SPACING),
            strokeWidth = with(density) { 1.dp.toPx() },
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 1f)
        )

        // Draw a marker
        drawScope.drawCircle(
            color = Color.Black,
            center = Offset(x, y),
            radius = with(density) { 5.dp.toPx() }
        )
    }
}

/**
 * Helper function to calculate the step size based on the number of days since the earliest date
 */
fun getStepSize(earliestDateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.US)
    val earliestDate = LocalDate.parse(earliestDateString, formatter)
    val now = LocalDate.now()
    val daysSinceEarliestDate = ChronoUnit.DAYS.between(earliestDate, now)
    return when {
        daysSinceEarliestDate <= 4 -> 8 //TimePeriod.ONE_DAY
        daysSinceEarliestDate <= 12 -> 32 //TimePeriod.FIVE_DAY
        daysSinceEarliestDate <= 40 -> 7 //TimePeriod.ONE_MONTH
        daysSinceEarliestDate <= 200 -> 30 //TimePeriod.THREE_MONTH, TimePeriod.SIX_MONTH
        daysSinceEarliestDate <= 380 -> 60 //TimePeriod.ONE_YEAR
        daysSinceEarliestDate <= 1850 -> 240 //TimePeriod.FIVE_YEAR
        daysSinceEarliestDate <= 4000 -> 480 //TimePeriod.TEN_YEAR
        else -> { //TimePeriod.MAX
            val yearsSinceEarliestDate = ChronoUnit.YEARS.between(earliestDate, now)
            val decadesSinceEarliestDate = yearsSinceEarliestDate / 10
            480 * (decadesSinceEarliestDate.toInt() + 1) // Adjust step size based on the number of decades
        }
    }
}

/**
 * Helper function to format the date based on the number of days since the earliest date
 * @see formatDateForTime
 * @see formatDateForMonth
 * @see formatDateForYear
 */
fun formatDate(date: String, earliestDateString: String): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a", Locale.US)
    val earliestDate = LocalDate.parse(earliestDateString, formatter)
    val now = LocalDate.now()
    val daysSinceEarliestDate = ChronoUnit.DAYS.between(earliestDate, now)
    return when {
        daysSinceEarliestDate <= 4 -> formatDateForTime(date) //TimePeriod.ONE_DAY
        daysSinceEarliestDate <= 12 -> formatDateForMonth(date) //TimePeriod.FIVE_DAY
        daysSinceEarliestDate <= 40 -> formatDateForMonth(date) //TimePeriod.ONE_MONTH
        else -> formatDateForYear(date)
    }
}

/**
 * Helper function to format the date for time in the form of "h:mm a"
 * Ex. "Jul 1, 2021 9:30 AM" -> "9:30 AM"
 */
fun formatDateForTime(date: String): String {
    val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    val parsedDate = formatter.parse(date)
    val outputFormatter = SimpleDateFormat("h:mm a", Locale.US)
    return parsedDate?.let { outputFormatter.format(it) } ?: date
}

/**
 * Helper function to format the date for month in the form of "MMM d"
 * Ex. "Jul 1, 2021 9:30 AM" -> "Jul 1"
 */
fun formatDateForMonth(date: String): String {
    val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    val parsedDate = formatter.parse(date)
    val outputFormatter = SimpleDateFormat("MMM d", Locale.US)
    return parsedDate?.let { outputFormatter.format(it) } ?: date
}

/**
 * Helper function to format the date for year in the form of "MMM yyyy"
 * Ex. "Jul 1, 2021 9:30 AM" -> "Jul 2021"
 */
fun formatDateForYear(date: String): String {
    val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.US)
    val parsedDate = formatter.parse(date)
    val outputFormatter = SimpleDateFormat("MMM yyyy", Locale.US)
    return parsedDate?.let { outputFormatter.format(it) } ?: date
}

/**
 * Helper function to format the volume in a more readable format
 * Ex. 12345678 -> "12.35M"
 */
fun formatVolume(volume: Long): String {
    return when {
        volume >= 1_000_000_000_000 -> String.format(
            locale = Locale.US,
            "%.2fT",
            volume / 1_000_000_000_000.0
        )

        volume >= 1_000_000_000 -> String.format(
            locale = Locale.US,
            "%.2fB",
            volume / 1_000_000_000.0
        )

        volume >= 1_000_000 -> String.format(locale = Locale.US, "%.2fM", volume / 1_000_000.0)
        volume >= 1_000 -> String.format(locale = Locale.US, "%.2fK", volume / 1_000.0)
        else -> volume.toString()
    }
}