package com.oldmen.stayintouch.presentation.ui.favorites

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.oldmen.stayintouch.R
import com.oldmen.stayintouch.domain.models.FavoriteArticle
import com.oldmen.stayintouch.presentation.mvp.favorites.FavoritesPresenter
import com.oldmen.stayintouch.utils.ARTICLE_DATE_FORMAT
import com.oldmen.stayintouch.utils.DateFormatter
import com.oldmen.stayintouch.utils.GlideApp
import com.oldmen.stayintouch.utils.ISO_DATE_FORMAT


class FavoriteArticleAdapter(var articles: List<FavoriteArticle>,
                             val presenter: FavoritesPresenter) : RecyclerView.Adapter<FavoriteArticleAdapter.FavoritesArticlesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesArticlesHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_recycler_item, parent, false)
        return FavoritesArticlesHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: FavoritesArticlesHolder, position: Int) {
        holder.bindView(articles[position])
    }

    fun updateData(newArticles: List<FavoriteArticle>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    inner class FavoritesArticlesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var logo: ImageView = itemView.findViewById(R.id.logo_article)
        var title: TextView = itemView.findViewById(R.id.title_article)
        var description: TextView = itemView.findViewById(R.id.description_article)
        var favoriteBtn: ImageButton = itemView.findViewById(R.id.favorite_btn_article)
        var shareBtn: ImageButton = itemView.findViewById(R.id.share_btn_article)
        var fbBtn: ImageButton = itemView.findViewById(R.id.facebook_btn)
        var fbShare: ShareButton = itemView.findViewById(R.id.facebook_share)
        var time: TextView = itemView.findViewById(R.id.time_article)

        fun bindView(article: FavoriteArticle) {
            //Init text fields
            title.text = article.title
            description.text = article.description
            time.text = DateFormatter.formatStrToStr(ISO_DATE_FORMAT, ARTICLE_DATE_FORMAT, article.publishedAt)
            //Init article image
            if (article.urlToImage.isNotEmpty())
                GlideApp.with(itemView)
                        .load(article.urlToImage)
                        .placeholder(R.drawable.loading_preview)
                        .thumbnail()
                        .onlyRetrieveFromCache(true)
                        .into(logo)
            else logo.setImageDrawable(itemView.resources.getDrawable(R.drawable.loading_preview))
            logo.setOnClickListener { _ ->
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.url)))
            }
            //Init favorite buttons
            favoriteBtn.setImageResource(R.drawable.ic_delete)
            favoriteBtn.setOnClickListener {
                presenter.deleteFromFavorite(article)
            }
            //Init facebook btn
            fbShare.shareContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(article.url))
                    .build()
            fbBtn.setOnClickListener { _ -> fbShare.performClick() }
            //Init share btn
            shareBtn.setOnClickListener { _ -> shareLink(article.url, article.title) }
        }

        private fun shareLink(link: String, title: String) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, link)
            sendIntent.type = "text/plain"
            itemView.context.startActivity(Intent.createChooser(sendIntent, title))

        }
    }
}