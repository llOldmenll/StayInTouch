package com.oldmen.stayintouch.presentation.ui.main

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.data.local.UserSessionUtils
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.domain.models.Source
import com.oldmen.stayintouch.presentation.mvp.main.MainPresenter
import com.oldmen.stayintouch.presentation.mvp.main.MainView
import com.oldmen.stayintouch.presentation.ui.base.BaseActivity
import com.oldmen.stayintouch.presentation.ui.favorites.FavoritesActivity
import com.oldmen.stayintouch.utils.ARTICLE_DATE_FORMAT
import com.oldmen.stayintouch.utils.DateFormatter
import com.oldmen.stayintouch.utils.ISO_DATE_FORMAT
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.back_drop_filter.*
import java.util.*

class MainActivity : BaseActivity(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var adapter: ArticlesAdapter

    private var isLoading = false
    private val maxPage = 999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initRecycler()
        initSortedSpinner()
        initDateFilters()
    }

    private fun initToolbar() {
        setSupportActionBar(app_bar)
        app_bar.setNavigationOnClickListener(NavigationIconClickListener(
                this,
                articles_list,
                AccelerateDecelerateInterpolator(),
                getDrawable(R.drawable.ic_drawer_open),
                getDrawable(R.drawable.ic_drawer_close),
                presenter))
    }

    private fun initRecycler() {
        val linearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val gridManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        adapter = ArticlesAdapter(emptyList(), object : ArticlesAdapter.ArticleListener {
            override fun onGlobalShareClicked(articleUrl: String, articleTitle: String) {
                globalUrlShare(articleUrl, articleTitle)
            }

            override fun onFavoriteClicked(article: Article) {
                article.isFavorite = !article.isFavorite
                presenter.updateFavorite(article)
            }

            override fun onOpenArticleClicked(articleUrl: String) {
                openArticle(articleUrl)
            }

        })
        recycler_view.layoutManager =
                if (resources.configuration.orientation == ORIENTATION_PORTRAIT) linearManager
                else gridManager

        recycler_view.adapter = adapter
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() >= adapter.itemCount - 5
                        && UserSessionUtils.getPage() < maxPage && !isLoading) {
                    isLoading = true
                    presenter.loadNextPage()
                }
            }
        })
        presenter.subscribeToArticles(this)
    }

    private fun initSortedSpinner() {
        val keys = resources.getStringArray(R.array.sorted_by_keys)
        val values = resources.getStringArray(R.array.sorted_by_values)

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, keys)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_drop)
        sorted_spinner!!.adapter = spinnerAdapter
        sorted_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                UserSessionUtils.setSortBy(values[0])
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                UserSessionUtils.setSortBy(values[p2])
            }

        }

        if (UserSessionUtils.isSessionCreated() && UserSessionUtils.getSortedBy().isNotEmpty()) {
            val currentValue = UserSessionUtils.getSortedBy()
            for (i in 0..values.size) {
                if (values[i].equals(currentValue)) {
                    sorted_spinner.setSelection(i)
                    break
                }
            }
        }
    }

    private fun initDateFilters() {
        val fromDate = UserSessionUtils.getFrom()
        val toDate = UserSessionUtils.getTo()

        if (fromDate.isNotEmpty())
            from_date_text.setText(DateFormatter.formatStrToStr(ISO_DATE_FORMAT, ARTICLE_DATE_FORMAT, fromDate))
        else
            from_date_text.setText("")

        if (toDate.isNotEmpty())
            to_date_text.setText(DateFormatter.formatStrToStr(ISO_DATE_FORMAT, ARTICLE_DATE_FORMAT, toDate))
        else
            to_date_text.setText("")

        btn_clear_from.setOnClickListener { _ -> presenter.clearFromDate() }

        btn_clear_to.setOnClickListener { _ -> presenter.clearToDate() }

        from_date_text.setOnClickListener {
            val calendar = Calendar.getInstance()

            if (UserSessionUtils.getFrom().isNotEmpty())
                calendar.time = DateFormatter.getDate(ISO_DATE_FORMAT, UserSessionUtils.getFrom())

            createDatePicker(calendar, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if (presenter.validateDate(calendar.time, true))
                    from_date_text.setText(DateFormatter.formatDateToStr("dd-MMM-yyyy", calendar.time))
            })
        }

        to_date_text.setOnClickListener {
            val calendar = Calendar.getInstance()

            if (UserSessionUtils.getTo().isNotEmpty())
                calendar.time = DateFormatter.getDate(ISO_DATE_FORMAT, UserSessionUtils.getTo())

            createDatePicker(calendar, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if (presenter.validateDate(calendar.time, false))
                    to_date_text.setText(DateFormatter.formatDateToStr("dd-MMM-yyyy", calendar.time))
            })
        }
    }

    private fun createDatePicker(calendar: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.favorites)
            startActivity(Intent(this, FavoritesActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun updateRecycler(articles: List<Article>) {
        adapter.updateData(articles)
        if (isLoading) hideProgress()
        isLoading = false
    }

    override fun setSources(sources: List<Source>) {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
                sources.map { src -> src.name }.toList())
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_drop)
        source_spinner!!.adapter = spinnerAdapter
        source_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                UserSessionUtils.setSource(sources[0].id)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                UserSessionUtils.setSource(sources[p2].id)
            }

        }

        if (UserSessionUtils.isSessionCreated() && UserSessionUtils.getSource().isNotEmpty()) {
            val currentValue = UserSessionUtils.getSource()
            for (i in 0..sources.size) {
                if (sources[i].id.equals(currentValue)) {
                    source_spinner.setSelection(i)
                    break
                }
            }
        }
    }

    override fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showNoInternetDialog() {
        hideProgress()
        AlertDialog.Builder(this, R.style.DialogTheme)
                .setTitle(getString(R.string.internet))
                .setMessage(getString(R.string.no_internet_msg))
                .setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok), null)
                .show()
    }

    override fun showProgress() {
        progress.visibility = VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = INVISIBLE
    }

    override fun clearFromDate() {
        from_date_text.setText("")
    }

    override fun clearToDate() {
        to_date_text.setText("")
    }
}
