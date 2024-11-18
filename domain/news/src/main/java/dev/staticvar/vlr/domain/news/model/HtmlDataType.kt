package dev.staticvar.vlr.domain.news.model

public sealed class HtmlDataType

public data class Paragraph(val text: String) : HtmlDataType()

public data class ListItem(val text: String) : HtmlDataType()

public data class Tweet(val tweetUrl: String) : HtmlDataType()

public data class Unknown(val text: String) : HtmlDataType()

public data class Heading(val text: String) : HtmlDataType()

public data class Video(val link: String) : HtmlDataType()

public data class Subtext(val text: String) : HtmlDataType()

public data class Quote(val text: String) : HtmlDataType()
