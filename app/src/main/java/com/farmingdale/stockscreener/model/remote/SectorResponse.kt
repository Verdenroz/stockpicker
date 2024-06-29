package com.farmingdale.stockscreener.model.remote

import kotlinx.serialization.Serializable

/**
 * Remote data class for sector information
 * @param sector the sector name
 * @param dayReturn the return for the day
 * @param ytdReturn the return year to date
 * @param yearReturn the return for the year
 * @param threeYearReturn the return for the past three years
 * @param fiveYearReturn the return for the past five years
 */
@Serializable
data class SectorResponse(
    val sector: String,
    val dayReturn: String,
    val ytdReturn: String,
    val yearReturn: String,
    val threeYearReturn: String,
    val fiveYearReturn: String
)
