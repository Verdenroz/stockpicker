package com.farmingdale.stockscreener.utils

/**
 * Represents an error during execution
 */
sealed interface Error


/**
 * Represents an error that occurred during data retrieval.
 */
sealed interface DataError: Error {
    enum class Network: DataError {
        NO_INTERNET,
        TIMEOUT,
        BAD_REQUEST,
        DENIED,
        NOT_FOUND,
        THROTTLED,
        SERVER_DOWN,
        SERIALIZATION,
        UNKNOWN,
    }
}