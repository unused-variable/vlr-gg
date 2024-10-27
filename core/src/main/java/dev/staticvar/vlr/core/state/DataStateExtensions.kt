package dev.staticvar.vlr.core.state

fun <T> DataState<T>.onEmpty(block: () -> Unit): DataState<T> {
  if (this is DataState.Empty) block()
  return this
}

fun <T> DataState<T>.onSuccess(block: (T) -> Unit): DataState<T> {
  if (this is DataState.Success) block(data)
  return this
}

fun <T> DataState<T>.onError(block: (String?, Exception?) -> Unit): DataState<T> {
  if (this is DataState.Error) block(message, exception)
  return this
}