package com.oldmen.stayintouch.domain.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.oldmen.stayintouch.utils.ARTICLES_TABLE_NAME

@Entity(tableName = ARTICLES_TABLE_NAME)
class Article(@PrimaryKey(autoGenerate = true) var id: Int) {
}