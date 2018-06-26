package com.oldmen.stayintouch.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.oldmen.stayintouch.domain.models.Article

@Database(entities = [Article::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

}