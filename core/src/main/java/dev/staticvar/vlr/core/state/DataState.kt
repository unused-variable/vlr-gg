package dev.staticvar.vlr.core.state

import java.time.LocalDateTime

/**
 * Monad to represent data of a type `<T>`
 *   - Empty: No data info, possibly data could be loading
 *   - Success: Cached or fetched data with optional loading state
 *   - Error: Error state with optional message and exception
 *
 * @param T
 * @constructor Create empty Data state
 */
sealed class DataState<T> private constructor() {

  /**
   * Timestamp of the data state object
   */
  val timeStamp: LocalDateTime = LocalDateTime.now()

  /**
   * Empty state of [DataState] object
   */
  data object Empty : DataState<Nothing>()

  /**
   * Success state of [DataState] to represent cached or updated data with optional loading state
   *
   * @param data
   * @param isLoading
   */
  data class Success<T>(val data: T, val isLoading: Boolean = false) : DataState<T>()

  /**
   * Error state of [DataState] to represent error with optional message and exception
   *
   * @param message
   * @param exception
   */
  data class Error<T>(val message: String? = null, val exception: Exception? = null) :
    DataState<T>()
}
