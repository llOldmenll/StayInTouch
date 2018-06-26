package com.oldmen.stayintouch.utils

import android.content.Context
import com.oldmen.stayintouch.CustomApplication
import android.net.ConnectivityManager


object InternetConnection {

    fun checkConnection(): Boolean {
        val manager = (CustomApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        return manager.activeNetworkInfo != null
    }

}