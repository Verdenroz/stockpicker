package com.farmingdale.stockscreener.utils

typealias RootError = Error

/**
 * Represents the state of a resource that is provided to the UI from the data layer.
 */
sealed interface Resource<out D, out E : RootError> {
    /**
     * Represents a successful retrieval of a resource.
     * @param data The data that was retrieved.
     */
    data class Success<out D>(val data: D) : Resource<D, Nothing>

    /**
     * Represents an error that occurred during the retrieval of a resource.
     * @param error The error that occurred.
     */
    data class Error<out E : RootError>(val error: RootError) : Resource<Nothing, E>

    /**
     * Represents a loading state of a resource.
     */
    data class Loading(val isLoading: Boolean = true) : Resource<Nothing, Nothing>
}