package com.example.test_app.network

import com.example.test_app.data.JsonWrapper
import com.example.test_app.data.Photo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("curated")
    suspend fun getPhotoList(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = 50
    ): JsonWrapper<Photo>
}