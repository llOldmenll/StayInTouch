package com.oldmen.stayintouch.presentation.mvp.main

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.oldmen.stayintouch.CustomApplication
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.data.local.UserSessionUtils
import com.oldmen.stayintouch.data.network.RetrofitClient
import com.oldmen.stayintouch.domain.models.*
import com.oldmen.stayintouch.utils.DateFormatter
import com.oldmen.stayintouch.utils.ISO_DATE_FORMAT
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            val sources = getSourcesFromDbAsync().await()
            viewState.setSources(sources)
        }
    }

    fun subscribeToArticles(lifecycleOwner: LifecycleOwner) {
        getArticles().observe(lifecycleOwner, Observer { articles ->
            if (articles != null) viewState.updateRecycler(articles)
        })
    }

    fun loadNextPage(dropArticlesTable: Boolean = false) {
        launch(Dispatchers.Main) {
            try {
                if (dropArticlesTable) dropArticlesAsync().await()
                val favoritesArticles = getFavoritesAsync().await()
                val articles = loadArticlesAsync().await().articles
                articles.map { article ->
                    if (favoritesArticles.contains(article.toFavoriteArticle()))
                        article.isFavorite = true
                }
                saveArticlesAsync(articles).await()
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
    }

    fun validateDate(date: Date, isFromDate: Boolean): Boolean {
        if (date.after(Calendar.getInstance().time)) {
            viewState.showMsg(CustomApplication.getContext().getString(R.string.out_of_bound_date_range_msg))
            return false
        } else if (isFromDate) {
            return when {
                UserSessionUtils.getTo().isEmpty() -> {
                    UserSessionUtils.setFrom(DateFormatter.formatDateToStr(ISO_DATE_FORMAT, date))
                    true
                }
                date.after(DateFormatter.getDate(ISO_DATE_FORMAT, UserSessionUtils.getTo())) -> {
                    viewState.showMsg(CustomApplication.getContext().getString(R.string.from_after_to_date_msg))
                    false
                }
                else -> {
                    UserSessionUtils.setFrom(DateFormatter.formatDateToStr(ISO_DATE_FORMAT, date))
                    true
                }
            }
        } else if (!isFromDate) {
            return when {
                UserSessionUtils.getFrom().isEmpty() -> {
                    UserSessionUtils.setTo(DateFormatter.formatDateToStr(ISO_DATE_FORMAT, date))
                    true
                }
                date.before(DateFormatter.getDate(ISO_DATE_FORMAT, UserSessionUtils.getFrom())) -> {
                    viewState.showMsg(CustomApplication.getContext().getString(R.string.to_before_from_date_msg))
                    false
                }
                else -> {
                    UserSessionUtils.setTo(DateFormatter.formatDateToStr(ISO_DATE_FORMAT, date))
                    true
                }
            }
        } else return false
    }

    fun loadArticlesIfNeeded() {
        if (UserSessionUtils.getPage() == 0) {
            viewState.showProgress()
            loadNextPage(true)
        }
    }

    fun updateFavorite(article: Article) {
        launch {
            withContext(Dispatchers.Default) {
                val db = CustomApplication.dataBase
                db.getArticleDao().update(article)
                if (article.isFavorite)
                    db.getFavoriteArticleDao().insert(article.toFavoriteArticle())
                else
                    db.getFavoriteArticleDao().delete(article.toFavoriteArticle())
            }
        }
    }

    fun clearFromDate() {
        if (UserSessionUtils.getFrom().isNotEmpty()) {
            UserSessionUtils.setFrom("")
            UserSessionUtils.setPage(0)
            viewState.clearFromDate()
        }
    }

    fun clearToDate() {
        if (UserSessionUtils.getTo().isNotEmpty()) {
            UserSessionUtils.setTo("")
            UserSessionUtils.setPage(0)
            viewState.clearToDate()
        }
    }

    private fun getArticles(): LiveData<List<Article>> {
        return CustomApplication.dataBase.getArticleDao().getAll()
    }

    private fun getSourcesFromDbAsync(): Deferred<List<Source>> {
        return async {
            CustomApplication.dataBase.getSourceDao().getAll()
        }
    }

    private fun loadArticlesAsync(): Deferred<ArticlesResponse> {
        val session: UserSession

        if (UserSessionUtils.isSessionCreated()) {
            session = UserSessionUtils.getSession()
        } else {
            session = UserSession()
            UserSessionUtils.saveSession(session)
        }
        session.page += 1
        UserSessionUtils.saveSession(session)

        return RetrofitClient.getApiService().getArticlesAsync(session.sortBy, session.source,
                session.pageSize, session.page, session.from, session.to)
    }

    private fun saveArticlesAsync(articles: List<Article>): Deferred<Unit> {
        return async {
            val dao = CustomApplication.dataBase.getArticleDao()
            dao.insertAll(articles)
        }
    }

    private fun dropArticlesAsync(): Deferred<Unit> {
        return async { CustomApplication.dataBase.getArticleDao().drop() }
    }

    private fun getFavoritesAsync(): Deferred<List<FavoriteArticle>> {
        return async {
            CustomApplication.dataBase.getFavoriteArticleDao().getList()
        }
    }
}