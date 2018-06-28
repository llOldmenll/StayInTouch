package com.oldmen.stayintouch.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.utils.ARTICLES_TABLE_NAME
import kotlinx.coroutines.experimental.Deferred

@Dao
interface ArticleDao {

    @Query("SELECT * from $ARTICLES_TABLE_NAME")
    fun getAll(): LiveData<List<Article>>

    @Query("SELECT * FROM $ARTICLES_TABLE_NAME WHERE url = :url LIMIT 1")
    fun getWithUrl(url: String): Article

    @Update
    fun update(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sources: List<Article>)

    @Query("DELETE from $ARTICLES_TABLE_NAME")
    fun drop()

}