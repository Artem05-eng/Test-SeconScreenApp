package com.example.test_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.test_app.data.Photo

@Database(
    entities = [Photo::class],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "unsplash-database"
    }
}