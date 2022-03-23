package com.example.test_app.network

import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor: Interceptor {

    companion object {
        const val API_KEY = "563492ad6f91700001000001abf4336c1a324dd1b050bd8e87e833dc"
        //const val API_KEY = "563492ad6f91700001000001f3dd48a4aa1d4995af772df25f9101fb"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifyRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "${API_KEY}")
            .build()
        return chain.proceed(modifyRequest)
    }
}