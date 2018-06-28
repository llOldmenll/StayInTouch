package com.oldmen.stayintouch.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import com.oldmen.stayintouch.domain.models.Source

@Database(entities = [Source::class, Article::class, FavoriteArticle::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    abstract fun getSourceDao(): SourceDao

    abstract fun getFavoriteArticleDao(): FavoriteArticleDao
}