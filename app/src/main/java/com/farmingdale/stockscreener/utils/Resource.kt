package com.farmingdale.stockscreener.utils

typealias RootError = Error

/**
 * Represents the state of a resource that is provided to the UI from the data layer.
 */
sealed interface Resource<out D, out E : RootError> {
    /**
     * Represents a successful retrieval of a resource.
     */
    data class Success<out D, out E : RootError>(val data: D) : Resource<D, E>

    /**
     * Represents an error that occurred during the retrieval of a resource.
     */
    data class Error<out D, out E : RootError>(val error: RootError, val data: D? = null) : Resource<D, E>

    /**
     * Represents a loading state of a resource.
     */
    data class Loading<out D, out E : RootError>(val isLoading: Boolean = true) : Resource<D, E>
}