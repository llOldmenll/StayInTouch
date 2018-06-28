package com.oldmen.stayintouch.presentation.mvp.main

import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.domain.models.Source
import com.oldmen.stayintouch.presentation.mvp.base.BaseView

interface MainView : BaseView {

    fun updateRecycler(articles: List<Article>)

    fun setSources(sources: List<Source>)

    fun showProgress()

    fun hideProgress()

}