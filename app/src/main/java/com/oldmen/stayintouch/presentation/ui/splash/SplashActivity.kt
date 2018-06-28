package com.oldmen.stayintouch.presentation.ui.splash

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.presentation.mvp.splash.SplashPresenter
import com.oldmen.stayintouch.presentation.mvp.splash.SplashView
import com.oldmen.stayintouch.presentation.ui.main.MainActivity


class SplashActivity : MvpAppCompatActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showNoInternetDialog() {
        AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle(getString(R.string.internet))
                .setMessage(getString(R.string.no_internet_msg))
                .setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok))
                { _, _ -> startMainActivity() }
                .setNegativeButton(getString(android.R.string.cancel))
                { _, _ ->
                    finish()
                }
                .show()
    }
}
