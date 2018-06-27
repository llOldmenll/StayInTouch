package com.oldmen.stayintouch.presentation.ui.main

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.arellomobile.mvp.MvpAppCompatActivity
import com.oldmen.stayintouch.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar)
        app_bar.setNavigationOnClickListener(NavigationIconClickListener(
                this,
                articles_list,
                AccelerateDecelerateInterpolator(),
                getDrawable(R.drawable.drawer_open_icon), // Menu open icon
                getDrawable(R.drawable.drawer_close_icon))) // Menu close icon

    }
}
