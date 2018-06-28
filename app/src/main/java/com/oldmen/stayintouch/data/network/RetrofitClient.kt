package com.oldmen.stayintouch.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.oldmen.stayintouch.utils.API_BASE_URL
import com.oldmen.stayintouch.utils.NullToEmptyStringAdapter
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private val interceptor = Interceptor { chain ->
        chain.proceed(chain.request()
                .newBuilder()
                .addHeader("X-Api-Key", "c96ce326bd064a8d87cd8b69a9b02784")
                .build())
    }

    private val moshi = Moshi.Builder()
            .add(NullToEmptyStringAdapter)
            .build()

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        return retrofit.create(ApiService::class.java)
    }

}