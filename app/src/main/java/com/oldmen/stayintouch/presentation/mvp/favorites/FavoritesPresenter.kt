package com.oldmen.stayintouch.presentation.mvp.favorites

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

@InjectViewState
class FavoritesPresenter(private val lifecycleOwner: LifecycleOwner) : MvpPresenter<FavoritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        CustomApplication.dataBase.getFavoriteArticleDao().getAll().observe(lifecycleOwner,
                Observer { favoriteArticles ->
                    if (favoriteArticles != null) viewState.updateRecycler(favoriteArticles)
                })
    }

    fun deleteFromFavorite(favoriteArticle: FavoriteArticle) {
        launch {
            withContext(DefaultDispatcher) {
                val db = CustomApplication.dataBase
                db.getFavoriteArticleDao().delete(favoriteArticle)
                db.getArticleDao().update(favoriteArticle.toArticle())
            }
        }
    }

}