package com.farmingdale.stockscreener.utils

import com.farmingdale.stockscreener.repos.base.FinanceQueryRepository
import com.farmingdale.stockscreener.repos.base.WatchlistRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Timer
import java.util.TimerTask

/**
 * A class that checks the market status and updates the refresh interval for the user's watch list
 */
class MarketStatusChecker(
    private val watchlistRepository: WatchlistRepository,
    private val financeQueryRepository: FinanceQueryRepository
) {
    private var timer: Timer? = null
    private var isMarketCurrentlyOpen: Boolean = isMarketOpen()

    /**
     * Start checking the market status
     */
    fun startChecking() {
        timer?.cancel() // Cancel the previous timer if it's still running
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    checkMarketStatus()
                }
            }, 0, 60000) // Check the market status every minute
        }
    }

    private fun checkMarketStatus() {
        val isMarketOpen = isMarketOpen()
        if (isMarketOpen != isMarketCurrentlyOpen) {
            isMarketCurrentlyOpen = isMarketOpen

            // The market status has changed, so update the refresh interval and restart the timer
            watchlistRepository.updateRefreshInterval(if (isMarketOpen) 30000L else 600000L)
            financeQueryRepository.updateRefreshInterval(if (isMarketOpen) 15000L else 1800000L)
            startChecking()
        }
    }
}

/**
 * Check if the market is currently open
 */
fun isMarketOpen(): Boolean {
    val now = LocalTime.now(ZoneId.systemDefault())
    val nowNewYork = now.atDate(LocalDate.now())
        .atZone(ZoneId.systemDefault())
        .withZoneSameInstant(ZoneId.of("America/New_York"))

    val isWeekday = nowNewYork.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
    val isBusinessHours = nowNewYork.toLocalTime().isAfter(LocalTime.of(9, 30)) && nowNewYork.toLocalTime().isBefore(LocalTime.of(16, 0))

    return isWeekday && isBusinessHours
}
