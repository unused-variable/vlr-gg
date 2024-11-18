package dev.staticvar.vlr.domain.news.repository

import dev.staticvar.vlr.domain.news.model.NewsArticle
import dev.staticvar.vlr.domain.news.model.NewsItem
import kotlinx.coroutines.flow.Flow

public interface NewsRepository {
    public fun getNews(): Flow<List<NewsItem>>
    public fun getNewsById(link: String): Flow<NewsArticle>
}