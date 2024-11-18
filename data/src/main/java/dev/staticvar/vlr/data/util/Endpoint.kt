package dev.staticvar.vlr.data.util

@JvmInline
value class Endpoint(private val url: String) {

  init {
    require(url.isNotEmpty()) { "URL must not be empty" }
  }

  override fun toString() = url
}

fun String.toEndpoint() = Endpoint(this)