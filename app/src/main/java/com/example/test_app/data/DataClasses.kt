package com.example.test_app.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class JsonWrapper<T> (
    val next_page: String?,
    val photos: List<T>
    )

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "photographer")
    val photographer: String,
    @ColumnInfo(name = "photographer_url")
    val photographer_url: String,
    @ColumnInfo(name = "photographer_id")
    val photographer_id: Int,
    @ColumnInfo(name = "alt")
    val alt: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "prev_key")
    var prev_key: Int?,
    @ColumnInfo(name = "next_key")
    var next_key: Int?,
    @Embedded val src: Source
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Source(
    val original: String,
    val medium: String
): Parcelable