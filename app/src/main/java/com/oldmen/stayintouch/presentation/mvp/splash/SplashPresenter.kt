package com.oldmen.stayintouch.presentation.mvp.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.data.local.UserSessionUtils
import com.oldmen.stayintouch.data.network.ApiService
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.domain.models.ArticlesResponse
import com.oldmen.stayintouch.domain.models.Source
import com.oldmen.stayintouch.domain.models.UserSession
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
            val sources = ApiService.create().getSources().await().sources
            saveSources(sources).await()
            val articles = loadArticles().await().articles
            saveArticles(articles)
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
            session = UserSessionUtils.getSession()
        } else {
            session = UserSession()
            UserSessionUtils.saveSession(session)
        }

        return ApiService.create().getArticles(session.sortBy, session.source, session.pageSize,
                session.page, session.from, session.to)
    }

    private fun saveSources(sources: List<Source>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getSourceDao()
            dao.deleteAll()
            dao.insertAll(sources)
        }
    }

    private fun saveArticles(articles: List<Article>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getArticleDao()
            dao.deleteAll()
            dao.insertAll(articles)
        }
    }

//    private fun getSourcesFromDb(): Deferred<List<Source>> {
//        return async {
//            CustomApplication.dataBase.getSourceDao().getAll()
//        }
//    }
//
//    private fun getArticlesFromDb(): Deferred<List<Article>> {
//        return async {
//            CustomApplication.dataBase.getArticleDao().getAll()
//        }
//    }

}