package com.oldmen.stayintouch.presentation.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import com.oldmen.stayintouch.presentation.mvp.favorites.FavoritesPresenter
import com.oldmen.stayintouch.presentation.mvp.favorites.FavoritesView
import kotlinx.android.synthetic.main.activity_main.*


class FavoritesActivity : MvpAppCompatActivity(), FavoritesView {

    @InjectPresenter
    lateinit var presenter: FavoritesPresenter
    private lateinit var adapter: FavoriteArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        initToolbar()
        initRecycler()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecycler() {
        val linearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val gridManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        adapter = FavoriteArticleAdapter(emptyList(), presenter)
        recycler_view.layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) linearManager
                else gridManager

        recycler_view.adapter = adapter
        presenter.subscribeToArticles(this)
    }

    override fun updateRecycler(favoriteArticles: List<FavoriteArticle>) {
        adapter.updateData(favoriteArticles)
    }
}
