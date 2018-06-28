package com.oldmen.stayintouch.presentation.mvp.main

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.domain.models.Source
import com.oldmen.stayintouch.presentation.mvp.base.BaseView

interface MainView : BaseView {

    fun updateRecycler(articles: List<Article>)

    fun setSources(sources: List<Source>)

    @StateStrategyType(SkipStrategy::class)
    fun showProgress()

    @StateStrategyType(SkipStrategy::class)
    fun hideProgress()

}