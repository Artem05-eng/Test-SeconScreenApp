package com.example.test_app.di

import androidx.room.Room
import com.example.test_app.app.App
import com.example.test_app.database.Database
import com.example.test_app.database.PhotoDao
import com.example.test_app.network.CustomInterceptor
import com.example.test_app.network.NetworkApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DataModule @Inject constructor(private val app: App) {

    @Singleton
    @Provides
    fun providesHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addNetworkInterceptor(CustomInterceptor())
        .followRedirects(true)
        .build()

    @Singleton
    @Provides
    fun providesNetworkApi(okHttpClient: OkHttpClient): NetworkApi = Retrofit.Builder()
        .baseUrl("https://api.pexels.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build().create()

    @Singleton
    @Provides
    fun providesDatabase(): Database {
        return Room.databaseBuilder(
            app,
            Database::class.java,
            "Database"
        ).build()
    }

    @Singleton
    @Provides
    fun providesPhotoDao(db: Database): PhotoDao = db.photoDao()

}