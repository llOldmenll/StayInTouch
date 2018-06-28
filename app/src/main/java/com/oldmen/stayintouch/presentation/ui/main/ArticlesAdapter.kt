package com.oldmen.stayintouch.presentation.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.presentation.mvp.main.MainPresenter
import com.oldmen.stayintouch.utils.ARTICLE_DATE_FORMAT
import com.oldmen.stayintouch.utils.DateFormatter
import com.oldmen.stayintouch.utils.GlideApp
import com.oldmen.stayintouch.utils.ISO_DATE_FORMAT


class ArticlesAdapter(var articles: List<Article>, val presenter: MainPresenter) : RecyclerView.Adapter<ArticlesAdapter.ArticlesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_recycler_item, parent, false)
        return ArticlesHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticlesHolder, position: Int) {
        holder.bindView(articles[position])
    }

    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    inner class ArticlesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var logo: ImageView = itemView.findViewById(R.id.logo_article)
        var title: TextView = itemView.findViewById(R.id.title_article)
        var description: TextView = itemView.findViewById(R.id.description_article)
        var favoriteBtn: ImageButton = itemView.findViewById(R.id.favorite_btn_article)
        var shareBtn: ImageButton = itemView.findViewById(R.id.share_btn_article)
        var time: TextView = itemView.findViewById(R.id.time_article)

        fun bindView(article: Article) {
            if (article.urlToImage.isNotEmpty())
                GlideApp.with(itemView)
                        .load(article.urlToImage)
                        .placeholder(R.drawable.loading_preview)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail()
                        .into(logo)
            else
                logo.setImageDrawable(itemView.resources.getDrawable(R.drawable.loading_preview))

            title.text = article.title
            description.text = article.description
            time.text = DateFormatter.formatStrToStr(ISO_DATE_FORMAT, ARTICLE_DATE_FORMAT, article.publishedAt)
            favoriteBtn.setImageResource(
                    if (article.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_border)
            favoriteBtn.setOnClickListener {
                article.isFavorite = !article.isFavorite
                presenter.updateFavorite(article)
            }
        }
    }
}