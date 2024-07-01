package com.farmingdale.stockscreener.utils

/**
 * Represents an error that occurred during the execution of a network request.
 */
sealed class Error(
    override val message: String? = null,
    override val cause: Throwable? = null,
    open val code: Int? = null
): Throwable() {

    /**
     * Represents an error that occurred due to a network issue such as no internet connection.
     */
    data class NetworkError(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : Error()

    /**
     * Represents an error that occurred due to an issue with the server.
     * @param code The 500 HTTP status code of the response.
     */
    data class ServerError(
        override val message: String? = null,
        override val cause: Throwable? = null,
        override val code: Int
    ) : Error()

    /**
     * Represents an error that occurred due to an issue with the client.
     * @param code The 400 HTTP status code of the response.
     */
    data class ClientError(
        override val message: String? = null,
        override val cause: Throwable? = null,
        override val code: Int
    ) : Error()

    /**
     * Represents an error that occurred due to an issue with the serialization or deserialization of data.
     */
    data class SerializationError(
        override val message: String? = null,
        override val cause: Throwable? = null,
    ) : Error()
}