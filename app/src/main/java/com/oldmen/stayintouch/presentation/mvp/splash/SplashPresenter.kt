package com.oldmen.stayintouch.presentation.mvp.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.data.local.UserSessionUtils
import com.oldmen.stayintouch.data.network.RetrofitClient
import com.oldmen.stayintouch.domain.models.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import retrofit2.HttpException
import java.io.IOException

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {

    override fun attachView(view: SplashView?) {
        super.attachView(view)
        launch(UI) {
            loadData()
        }
    }

    suspend fun loadData() {
        try {
            val sources = RetrofitClient.getApiService().getSources().await().sources
            saveSources(sources).await()
            val favoritesArticles = getFavorites().await()
            val articles = loadArticles().await().articles
            articles.map { article ->
                if (favoritesArticles.contains(article.toFavoriteArticle()))
                    article.isFavorite = true
            }
            articles.map { article -> println(article) }
            saveArticles(articles).await()
            viewState.startMainActivity()
        } catch (e: IOException) {
            e.printStackTrace()
            viewState.showNoInternetDialog()
        } catch (e: HttpException) {
            viewState.showMsg(e.message())
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadArticles(): Deferred<ArticlesResponse> {
        val session: UserSession

        if (UserSessionUtils.isSessionCreated()) {
            UserSessionUtils.resetPageCount()
            session = UserSessionUtils.getSession()
        } else {
            session = UserSession()
            UserSessionUtils.saveSession(session)
        }

        return RetrofitClient.getApiService().getArticles(session.sortBy, session.source,
                session.pageSize, session.page, session.from, session.to)
    }

    private fun getFavorites(): Deferred<List<FavoriteArticle>> {
        return async {
            CustomApplication.dataBase.getFavoriteArticleDao().getList()
        }
    }

    private fun saveSources(sources: List<Source>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getSourceDao()
            dao.insertAll(sources)
        }
    }

    private fun saveArticles(articles: List<Article>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getArticleDao()
            dao.drop()
            dao.insertAll(articles)
        }
    }

}