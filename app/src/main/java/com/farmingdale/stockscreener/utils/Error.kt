package com.farmingdale.stockscreener.utils

import com.farmingdale.stockscreener.R

/**
 * Represents an error during execution
 */
sealed interface Error {
    fun asUiText(): UiText {
        when (this) {
            is DataError -> return this.asString()
        }
    }
}


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

    fun DataError.asString(): UiText {
        return when (this) {
            Network.NO_INTERNET -> UiText.StringResource(R.string.no_internet)
            Network.TIMEOUT -> UiText.StringResource(R.string.timeout)
            Network.BAD_REQUEST -> UiText.StringResource(R.string.bad_request)
            Network.DENIED -> UiText.StringResource(R.string.denied)
            Network.NOT_FOUND -> UiText.StringResource(R.string.not_found)
            Network.THROTTLED -> UiText.StringResource(R.string.throttled)
            Network.SERVER_DOWN -> UiText.StringResource(R.string.server_down)
            Network.SERIALIZATION -> UiText.StringResource(R.string.serialization)
            Network.UNKNOWN -> UiText.StringResource(R.string.unknown_error)
        }
    }
}



