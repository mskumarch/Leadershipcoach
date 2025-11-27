package com.meetingcoach.leadershipconversationcoach.utils

/**
 * Result wrapper for operations that can fail
 * 
 * Provides type-safe error handling without exceptions.
 * Use this instead of try-catch-return-null pattern.
 * 
 * Example:
 * ```
 * fun parseJson(json: String): Result<Data> {
 *     return try {
 *         val data = JSONObject(json)
 *         Result.Success(data)
 *     } catch (e: Exception) {
 *         Result.Error("Invalid JSON", e)
 *     }
 * }
 * 
 * when (val result = parseJson(input)) {
 *     is Result.Success -> useData(result.data)
 *     is Result.Error -> showError(result.message)
 * }
 * ```
 */
sealed class Result<out T> {
    /**
     * Success case with data
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Error case with message and optional exception
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()
    
    /**
     * Loading state (for async operations)
     */
    object Loading : Result<Nothing>()
    
    // Helper methods
    
    /**
     * Returns data if Success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    /**
     * Returns data if Success, default value otherwise
     */
    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> data
        else -> default
    }
    
    /**
     * Returns data if Success, throws exception otherwise
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception ?: Exception(message)
        is Loading -> throw IllegalStateException("Cannot get value while loading")
    }
    
    /**
     * Transform success value
     */
    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(message, exception)
        is Loading -> Loading
    }
    
    /**
     * Execute action if Success
     */
    fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Execute action if Error
     */
    fun onError(action: (String, Throwable?) -> Unit): Result<T> {
        if (this is Error) action(message, exception)
        return this
    }
}
