package com.farmingdale.stockscreener.utils

import java.lang.Exception

/**
 * Raised when an error occurs during data retrieval.
 */
sealed class DataException(
    open val code: Int? = null,
): Exception()

/**
 * Represents an error that occurred during the execution of a network request.
 */
data class HttpException(
    override val cause: Throwable? = null,
    override val code: Int?
) : DataException()

/**
 * Represents an error that occurred due to a network issue such as no internet connection.
 */
data class NetworkException(
    override val cause: Throwable? = null,
) : DataException()

/**
 * Represents an error that occurred due to an unknown issue.
 */
data class UnknownException(
    override val cause: Throwable? = null,
) : DataException()