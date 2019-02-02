package com.oldmen.stayintouch.data.network

import com.oldmen.stayintouch.domain.models.ArticlesResponse
import com.oldmen.stayintouch.domain.models.SourcesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("sources")
    fun getSourcesAsync(): Deferred<SourcesResponse>

    @GET("everything")
    fun getArticlesAsync(@Query("sortBy") sortBy: String = "publishedAt",
                         @Query("sources") source: String = "abc-news",
                         @Query("pageSize") pageSize: Int = 10,
                         @Query("page") page: Int = 1,
                         @Query("from") from: String = "",
                         @Query("to") to: String = ""): Deferred<ArticlesResponse>

}