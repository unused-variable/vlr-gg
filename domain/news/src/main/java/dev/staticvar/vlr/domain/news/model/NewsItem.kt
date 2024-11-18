package dev.staticvar.vlr.domain.news.model

import androidx.annotation.Keep

@Keep
public data class NewsItem(
  val link: String = "", // /67532/report-bds-to-overhaul-roster
  val author: String = "", // Eutalyx
  val date: String = "", // January 29, 2022
  val description: String =
    "", // BDS are going international in 2022 after several months of unsuccessful results.
  val title: String = "", // Report: BDS to overhaul roster
)
