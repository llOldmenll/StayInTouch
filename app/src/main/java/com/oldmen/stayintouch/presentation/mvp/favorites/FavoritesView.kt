package com.oldmen.stayintouch.presentation.mvp.favorites

import com.arellomobile.mvp.MvpView
import com.oldmen.stayintouch.domain.models.FavoriteArticle

interface FavoritesView : MvpView {

    fun updateRecycler(favoriteArticles: List<FavoriteArticle>)

}