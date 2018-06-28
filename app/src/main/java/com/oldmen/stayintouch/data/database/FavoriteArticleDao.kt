package com.oldmen.stayintouch.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import com.oldmen.stayintouch.utils.FAVORITE_ARTICLES_TABLE_NAME

@Dao
interface FavoriteArticleDao {

    @Query("SELECT * FROM $FAVORITE_ARTICLES_TABLE_NAME")
    fun getAll(): LiveData<List<FavoriteArticle>>

    @Query("SELECT * FROM $FAVORITE_ARTICLES_TABLE_NAME")
    fun getList(): List<FavoriteArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg articles: FavoriteArticle)

    @Delete
    fun delete(article: FavoriteArticle)

    @Query("DELETE from $FAVORITE_ARTICLES_TABLE_NAME")
    fun drop()

}