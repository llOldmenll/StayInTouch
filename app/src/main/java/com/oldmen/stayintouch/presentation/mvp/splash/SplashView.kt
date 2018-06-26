package com.oldmen.stayintouch.presentation.mvp.splash

import com.oldmen.stayintouch.presentation.mvp.base.BaseView

interface SplashView : BaseView {

    fun showMsg(msg: String)

    fun showNoInternetDialog()

    fun startMainActivity()

}