package dev.staticvar.vlr.domain.news.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
public data class NewsResponseItem(
  @SerialName("url") val link: String = "", // /67532/report-bds-to-overhaul-roster
  @SerialName("author") val author: String = "", // Eutalyx
  @SerialName("date") val date: String = "", // January 29, 2022
  @SerialName("description")
  val description: String =
    "", // BDS are going international in 2022 after several months of unsuccessful results.
  @SerialName("title") val title: String = "", // Report: BDS to overhaul roster
)
