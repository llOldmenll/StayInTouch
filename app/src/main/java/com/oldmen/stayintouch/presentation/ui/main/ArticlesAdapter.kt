package com.oldmen.stayintouch.presentation.ui.main

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.domain.models.Article
import com.oldmen.stayintouch.presentation.mvp.main.MainPresenter
import com.oldmen.stayintouch.utils.ARTICLE_DATE_FORMAT
import com.oldmen.stayintouch.utils.DateFormatter
import com.oldmen.stayintouch.utils.GlideApp
import com.oldmen.stayintouch.utils.ISO_DATE_FORMAT


class ArticlesAdapter(var articles: List<Article>, val listener: ArticleListener) : RecyclerView.Adapter<ArticlesAdapter.ArticlesHolder>() {

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
        private var logo: ImageView = itemView.findViewById(R.id.logo_article)
        private var title: TextView = itemView.findViewById(R.id.title_article)
        private var description: TextView = itemView.findViewById(R.id.description_article)
        private var favoriteBtn: ImageButton = itemView.findViewById(R.id.favorite_btn_article)
        private var shareBtn: ImageButton = itemView.findViewById(R.id.share_btn_article)
        private var fbBtn: ImageButton = itemView.findViewById(R.id.facebook_btn)
        private var fbShare: ShareButton = itemView.findViewById(R.id.facebook_share)
        private var time: TextView = itemView.findViewById(R.id.time_article)

        fun bindView(article: Article) {
            //Init text fields
            title.text = article.title
            description.text = article.description
            time.text = DateFormatter.formatStrToStr(ISO_DATE_FORMAT, ARTICLE_DATE_FORMAT, article.publishedAt)
            //Init article image
            if (article.urlToImage.isNotEmpty())
                GlideApp.with(itemView)
                        .load(article.urlToImage)
                        .placeholder(R.drawable.loading_preview)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail()
                        .into(logo)
            else logo.setImageDrawable(itemView.resources.getDrawable(R.drawable.loading_preview))
            logo.setOnClickListener { _ -> listener.onOpenArticleClicked(article.url) }
            //Init favorite btn
            favoriteBtn.setImageResource(
                    if (article.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_border)
            favoriteBtn.setOnClickListener { listener.onFavoriteClicked(article) }
            //Init facebook btn
            fbShare.shareContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(article.url))
                    .build()
            fbBtn.setOnClickListener { _ -> fbShare.performClick() }
            //Init share btn
            shareBtn.setOnClickListener { _ -> listener.onGlobalShareClicked(article.url, article.title) }
        }

    }

    interface ArticleListener {

        fun onGlobalShareClicked(articleUrl: String, articleTitle: String)

        fun onFavoriteClicked(article: Article)

        fun onOpenArticleClicked(articleUrl: String)
    }

}