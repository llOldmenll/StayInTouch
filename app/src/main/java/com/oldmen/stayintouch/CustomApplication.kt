package com.oldmen.stayintouch

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.support.multidex.MultiDex
import com.oldmen.stayintouch.data.database.AppDataBase
import com.oldmen.stayintouch.utils.DATA_BASE_NAME

class CustomApplication : Application() {

    companion object {
        private lateinit var appContext: Context
        lateinit var dataBase: AppDataBase
        fun getContext(): Context = appContext
    }

    override fun onCreate() {
        super.onCreate()
        dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, DATA_BASE_NAME).build()
        appContext = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}