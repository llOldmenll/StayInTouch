package com.oldmen.stayintouch.domain.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.oldmen.stayintouch.utils.SOURCE_TABLE_NAME

@Entity(tableName = SOURCE_TABLE_NAME)
data class Source(@PrimaryKey val id: String, val name: String)