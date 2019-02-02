package com.oldmen.stayintouch.presentation.mvp.favorites

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class FavoritesPresenter : MvpPresenter<FavoritesView>(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun subscribeToArticles(lifecycleOwner: LifecycleOwner) {
        CustomApplication.dataBase.getFavoriteArticleDao().getAll().observe(lifecycleOwner,
                Observer { favoriteArticles ->
                    if (favoriteArticles != null) viewState.updateRecycler(favoriteArticles)
                })
    }

    fun deleteFromFavorite(favoriteArticle: FavoriteArticle) {
        launch(Dispatchers.Default) {
            val db = CustomApplication.dataBase
            db.getFavoriteArticleDao().delete(favoriteArticle)
            db.getArticleDao().update(favoriteArticle.toArticle())
        }
    }

}