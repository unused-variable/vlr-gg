package dev.staticvar.vlr.domain.news.model

import androidx.annotation.Keep

@Keep
public data class NewsArticle(
  val title: String = "",
  val authorName: String = "",
  val time: String = "",
  val news: List<HtmlDataType>? = null
)