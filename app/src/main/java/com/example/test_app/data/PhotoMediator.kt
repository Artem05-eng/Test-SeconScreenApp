package com.example.test_app.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.test_app.database.Database
import com.example.test_app.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val initailPage: Int = 1

@ExperimentalPagingApi
class PhotoMediator (
    private val db: Database,
    private val network: NetworkApi
) : RemoteMediator<Int, Photo>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val photo = getClosestPhoto(state)
                photo?.next_key?.minus(1) ?: initailPage
            }
            LoadType.PREPEND -> {
                val photo = getPhotoForFirstItem(state)
                val prev_key = photo?.prev_key ?: return MediatorResult.Success(
                    endOfPaginationReached = (photo != null)
                )
                prev_key
            }
            LoadType.APPEND -> {
                val photo = getPhotoForLastItem(state)
                val next_key = photo?.next_key ?: return MediatorResult.Success(
                    endOfPaginationReached = (photo != null)
                )
                next_key
            }
        }
        try {
            val result = network.getPhotoList(page!!, 50)
            val photos = result.photos
            val endOfPaginationReached = page*50 >= 8000
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.photoDao().clearPhotos()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val photosDB = photos.map { photo ->
                    photo.prev_key = prevKey
                    photo.next_key = nextKey
                    photo
                }
                db.photoDao().insertPhotos(photosDB)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getPhotoForLastItem(state: PagingState<Int, Photo>): Photo? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { photo ->
                db.photoDao().getPhotoById(photo.id)
            }
    }

    private suspend fun getPhotoForFirstItem(state: PagingState<Int, Photo>): Photo? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.let { photo ->
                db.photoDao().getPhotoById(photo.id)
            }
    }

    private suspend fun getClosestPhoto(state: PagingState<Int, Photo>): Photo? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                db.photoDao().getPhotoById(photoId)
            }
        }
    }

}