package com.alperen.newsapp.repository

import com.alperen.newsapp.api.RetrofitInstance
import com.alperen.newsapp.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNum: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNum)

    suspend fun searchNews(searchQuery: String, pageNum: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNum)
}