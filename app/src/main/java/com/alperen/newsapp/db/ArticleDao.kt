package com.alperen.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alperen.newsapp.models.Article

@Dao
interface ArticleDao {

    // Update and Insert - upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}