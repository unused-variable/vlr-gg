package dev.staticvar.vlr.core.state

import androidx.compose.runtime.Composable
import dev.staticvar.vlr.core.utils.ComposableFunction

@ComposableFunction
@Composable
fun <T> DataState<T>.onEmptyComposable(block: @Composable () -> Unit): DataState<T> {
  if (this is DataState.Empty) block()
  return this
}

@ComposableFunction
@Composable
fun <T> DataState<T>.onSuccessComposable(block: @Composable (T) -> Unit): DataState<T> {
  if (this is DataState.Success) block(data)
  return this
}

@ComposableFunction
@Composable
fun <T> DataState<T>.onErrorComposable(block: @Composable (String?, Exception?) -> Unit): DataState<T> {
  if (this is DataState.Error) block(message, exception)
  return this
}