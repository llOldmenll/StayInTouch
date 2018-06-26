package com.oldmen.stayintouch.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.oldmen.stayintouch.domain.models.ArticlesResponse
import com.oldmen.stayintouch.domain.models.SourcesResponse
import com.oldmen.stayintouch.utils.API_BASE_URL
import kotlinx.coroutines.experimental.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("sources")
    fun getSources(): Deferred<SourcesResponse>

    @GET("everything")
    fun getArticles(@Query("sortBy") sortBy: String = "publishedAt",
                    @Query("sources") source: String = "abc-news",
                    @Query("pageSize") pageSize: Int = 10,
                    @Query("page") page: Int = 1,
                    @Query("from") from: String = "",
                    @Query("to") to: String = ""):Deferred<ArticlesResponse>

    companion object Factory {
        private val interceptor = Interceptor { chain ->
            chain.proceed(chain.request()
                    .newBuilder()
                    .addHeader("X-Api-Key", "c96ce326bd064a8d87cd8b69a9b02784")
                    .build())
        }

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}