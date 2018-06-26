package com.oldmen.stayintouch.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.utils.ARTICLES_TABLE_NAME

@Dao
interface ArticleDao {

    @Query("SELECT * from $ARTICLES_TABLE_NAME")
    fun getAll(): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sources: List<Article>)

    @Query("DELETE from $ARTICLES_TABLE_NAME")
    fun deleteAll()

}