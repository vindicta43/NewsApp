package com.alperen.newsapp.repository

import com.alperen.newsapp.api.RetrofitInstance
import com.alperen.newsapp.db.ArticleDatabase
import com.alperen.newsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNum: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNum)

    suspend fun searchNews(searchQuery: String, pageNum: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNum)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}