package com.oldmen.stayintouch.presentation.mvp.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.data.local.UserSessionUtils
import com.oldmen.stayintouch.data.network.RetrofitClient
import com.oldmen.stayintouch.domain.models.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    override fun attachView(view: SplashView?) {
        super.attachView(view)
        launch(Dispatchers.Main) {
            loadData()
        }
    }

    private suspend fun loadData() {
        try {
            val sources = RetrofitClient.getApiService().getSourcesAsync().await().sources
            saveSourcesAsync(sources).await()
            val favoritesArticles = getFavoritesAsync().await()
            val articles = loadArticlesAsync().await().articles
            articles.map { article ->
                if (favoritesArticles.contains(article.toFavoriteArticle()))
                    article.isFavorite = true
            }
            articles.map { article -> println(article) }
            saveArticlesAsync(articles).await()
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

    private fun loadArticlesAsync(): Deferred<ArticlesResponse> {
        val session: UserSession

        if (UserSessionUtils.isSessionCreated()) {
            UserSessionUtils.resetPageCount()
            session = UserSessionUtils.getSession()
        } else {
            session = UserSession()
            UserSessionUtils.saveSession(session)
        }

        return RetrofitClient.getApiService().getArticlesAsync(session.sortBy, session.source,
                session.pageSize, session.page, session.from, session.to)
    }

    private fun getFavoritesAsync(): Deferred<List<FavoriteArticle>> {
        return async {
            CustomApplication.dataBase.getFavoriteArticleDao().getList()
        }
    }

    private fun saveSourcesAsync(sources: List<Source>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getSourceDao()
            dao.insertAll(sources)
        }
    }

    private fun saveArticlesAsync(articles: List<Article>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getArticleDao()
            dao.drop()
            dao.insertAll(articles)
        }
    }

}