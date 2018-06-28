package com.oldmen.stayintouch.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.oldmen.stayintouch.domain.models.Source
import com.oldmen.stayintouch.utils.SOURCE_TABLE_NAME
import kotlinx.coroutines.experimental.Deferred

@Dao
interface SourceDao {

    @Query("SELECT * from $SOURCE_TABLE_NAME")
    fun getAll(): List<Source>

    @Insert(onConflict = REPLACE)
    fun insertAll(sources: List<Source>)

    @Query("DELETE from $SOURCE_TABLE_NAME")
    fun drop()

}