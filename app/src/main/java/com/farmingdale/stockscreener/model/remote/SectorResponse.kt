package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * Remote data class for sector information
 * @param sector the sector name
 * @param day_return the return for the day
 * @param ytd_return the return year to date
 * @param year_return the return for the year
 * @param three_year_return the return for the past three years
 * @param five_year_return the return for the past five years
 */
@Serializable
data class SectorResponse(
    val sector: String,
    val day_return: String,
    val ytd_return: String,
    val year_return: String,
    val three_year_return: String,
    val five_year_return: String,
)
