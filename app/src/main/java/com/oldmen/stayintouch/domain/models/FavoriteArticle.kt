package com.oldmen.stayintouch.domain.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.oldmen.stayintouch.utils.FAVORITE_ARTICLES_TABLE_NAME

@Entity(tableName = FAVORITE_ARTICLES_TABLE_NAME)
data class FavoriteArticle(@PrimaryKey val url: String,
                           val title: String = "",
                           val description: String = "",
                           val urlToImage: String = "",
                           val publishedAt: String = "") {

    fun toArticle(): Article {
        return Article(url, title, description, urlToImage, publishedAt, false)
    }

}