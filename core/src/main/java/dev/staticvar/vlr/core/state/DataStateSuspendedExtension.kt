package dev.staticvar.vlr.core.state

import dev.staticvar.vlr.core.utils.SuspendFunction

@SuspendFunction
suspend fun <T> DataState<T>.onEmptySuspended(block: suspend () -> Unit): DataState<T> {
  if (this is DataState.Empty) block()
  return this
}

@SuspendFunction
suspend fun <T> DataState<T>.onSuccessSuspended(block: suspend (T) -> Unit): DataState<T> {
  if (this is DataState.Success) block(data)
  return this
}

@SuspendFunction
suspend fun <T> DataState<T>.onErrorSuspended(block: suspend (String?, Exception?) -> Unit): DataState<T> {
  if (this is DataState.Error) block(message, exception)
  return this
}
