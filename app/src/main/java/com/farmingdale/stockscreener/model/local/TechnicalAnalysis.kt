package com.farmingdale.stockscreener.model.local

/**
 * Base class for technical analysis containing the date and value
 * @param date the date of the technical analysis
 * @param value the value of the technical analysis
 */
sealed class TechnicalAnalysis(
    open val date: String,
    open val value: String
)

/**
 * Simple Moving Average (SMA) is a of moving average that places a equal weight to all price points
 *
 * When the price crosses above the SMA, it can be interpreted as a potential bullish signal,
 * Conversely, a price crossing below the SMA might indicate a bearish signal.
 *
 * @see <a href="https://www.investopedia.com/terms/s/sma.asp">Investopedia</a>
 */
data class SMA(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * Exponential Moving Average (EMA) is a of moving average that places a greater weight on the most recent price points
 *
 * When the price crosses above the EMA, it can be interpreted as a potential bullish signal,
 * Conversely, a price crossing below the EMA might indicate a bearish signal.
 *
 * @see <a href="https://www.investopedia.com/terms/e/ema.asp">Investopedia</a>
 */
data class EMA(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * Stochastic oscillator (STOCH) measure the momentum of an asset's price to determine trends and predict reversals.
 * More used in sideways markets than trending markets.
 * Readings above 80 indicating that an asset is overbought and measurements below 20 indicating that it is oversold.
 *
 * @param value is empty here, use slowK and slowD instead
 * @param slowK the fast stochastic oscillator (K) is more sensitive to price movements
 * @param slowD the slow stochastic oscillator (D) is less sensitive but more reliable long term
 *
 * @see <a href="https://www.investopedia.com/terms/s/stochasticoscillator.asp">Investopedia</a>
 */
data class STOCH(
    override val date: String,
    override val value: String,
    val slowK: String,
    val slowD: String
) : TechnicalAnalysis(date, value)

/**
 * The relative strength index (RSI) is a momentum indicator to measure speed and strength of price movement.
 * More used in trending markets than sideways markets.
 *
 * Values under 30 indicate oversold conditions, while values over 70 indicate overbought conditions.
 *
 * @see <a href="https://www.investopedia.com/terms/r/rsi.asp">Investopedia</a>
 */
data class RSI(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * The Average Directional Index (ADX) is used to determine when the price is trending strongly.
 *
 * Readings above 25 indicate a strong trend is in place, while readings below 25 indicate weaker trends
 *
 * @see <a href="https://www.investopedia.com/terms/a/adx.asp">Investopedia</a>
 */
data class ADX(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * Commodity Channel Index (CCI) is momentum oscillator that indicates overbought/oversold levels and potential trend changes.
 *
 * Readings above +100 often suggest overbought conditions, while readings below -100 suggest oversold conditions.
 *
 * When CCI moves in the opposite direction of price, it can signal potential trend reversals.
 *
 *@see  <a href="https://www.investopedia.com/terms/c/commoditychannelindex.asp">Investopedia</a>
 */
data class CCI(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * The Aroon indicator (AROON) is composed of two lines. An up line which measures the number of periods since a High,
 * and a down line which measures the number of periods since a Low.
 *
 * When the Aroon Up is above the Aroon Down, it indicates bullish price behavior.
 * When the Aroon Down is above the Aroon Up, it signals bearish price behavior.
 *
 * @param value is empty here use aroonDown and arronUp instead
 * @param aroonDown the number of periods since a Low
 * @param arronUp the number of periods since a High
 *
 * @see <a href="https://www.investopedia.com/terms/a/aroon.asp">Investopedia</a>
 */
data class AROON(
    override val date: String,
    override val value: String,
    val aroonDown: String,
    val arronUp: String
) : TechnicalAnalysis(date, value)

/**
 * Bollinger bands (BBANDS) identify a stock's high and low volatility points.
 * Wider bands signal higher volatility, narrower bands signal lower volatility.
 *
 * Price touching or staying near the upper/lower bands suggests potential overbought/oversold conditions,
 * but not necessarily a guarantee of reversal.
 *
 * @param value is empty, use realUpperBand, realMiddleBand, and realLowerBand instead
 * @param realUpperBand a certain number of standard deviations above the middle band, indicating potential overbought levels.
 * @param realMiddleBand a moving average (usually simple) representing the typical price.
 * @param realLowerBand a certain number of standard deviations below the middle band, indicating potential oversold levels.
 * @see <a href="https://www.investopedia.com/terms/b/bollingerbands.asp">Investopedia</a>
 */
data class BBANDS(
    override val date: String,
    override val value: String,
    val realUpperBand: String,
    val realMiddleBand: String,
    val realLowerBand: String
) : TechnicalAnalysis(date, value)

/**
 * The Chaikin A/D line (AD) examines both the strength of price moves and underlying buying and selling pressure.
 *
 * A Chaikin Oscillator reading above zero indicates net buying pressure, while one below zero registers net selling pressure.
 * The divergence between the indicator and pure price moves are the most common signals from the indicator, and often flag market turning points.
 *
 * @see <a href="https://www.investopedia.com/terms/c/chaikinoscillator.asp">Investopedia</a>
 */
data class AD(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)

/**
 * On-balance volume (OBV) is a momentum indicator that measures positive and negative volume flow.
 *
 * The theory posits that when volume increases or decreases dramatically, without significant change in an issue's price,
 * at some point the price "springs" upward or downward.
 *
 * @see <a href="https://www.investopedia.com/terms/o/onbalancevolume.asp">Investopedia</a>
 */
data class OBV(
    override val date: String,
    override val value: String
) : TechnicalAnalysis(date, value)