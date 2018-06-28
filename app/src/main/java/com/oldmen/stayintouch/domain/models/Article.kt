package com.oldmen.stayintouch.domain.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.oldmen.stayintouch.utils.ARTICLES_TABLE_NAME

@Entity(tableName = ARTICLES_TABLE_NAME)
data class Article(@PrimaryKey val url: String,
                   val title: String = "",
                   val description: String = "",
                   val urlToImage: String = "",
                   val publishedAt: String = "",
                   var isFavorite: Boolean = false) {

    fun toFavoriteArticle(): FavoriteArticle {
        return FavoriteArticle(url, title, description, urlToImage, publishedAt)
    }

}