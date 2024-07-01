package com.farmingdale.stockscreener.utils

/**
 * Represents the state of a resource that is provided to the UI from the data layer.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful retrieval of a resource.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error that occurred during the retrieval of a resource.
     */
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)

    /**
     * Represents a loading state of a resource.
     */
    class Loading<T>(val isLoading: Boolean) : Resource<T>(null)
}