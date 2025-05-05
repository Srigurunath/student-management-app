package com.student.management.network

import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "http://192.168.29.175:5000/" // Flask IP address

    private var retrofit: Retrofit? = null
    fun getInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
