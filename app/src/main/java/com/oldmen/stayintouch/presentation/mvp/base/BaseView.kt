package com.oldmen.stayintouch.presentation.mvp.base

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView{

    fun showMsg(msg: String)

    fun showNoInternetDialog()

}