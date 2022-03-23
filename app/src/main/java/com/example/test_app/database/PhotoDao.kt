package com.example.test_app.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.test_app.data.Photo

@Dao
interface PhotoDao {

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhotoById(id: Int): Photo?

    @Query("SELECT * FROM photos")
    suspend fun getAllPhotos(): List<Photo>

    @Query("SELECT * FROM photos")
    fun getPagingPhoto(): PagingSource<Int, Photo>

}