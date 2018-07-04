package com.oldmen.stayintouch.presentation.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.MvpAppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : MvpAppCompatActivity() {

    protected fun globalUrlShare(articleUrl: String, articleTitle: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, articleUrl)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, articleTitle))
    }

    protected fun openArticle(articleUrl: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl)))
    }
}