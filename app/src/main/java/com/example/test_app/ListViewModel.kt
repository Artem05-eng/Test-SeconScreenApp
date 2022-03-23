package com.example.test_app

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.test_app.data.Photo
import com.example.test_app.data.PhotoMediator
import com.example.test_app.database.Database
import com.example.test_app.database.PhotoDao
import com.example.test_app.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val networkApi: NetworkApi,
    private val db: Database
): ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    fun getPhotosFromNetworkandDB(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(50),
            remoteMediator = PhotoMediator(db, networkApi)
        ) {
            db.photoDao().getPagingPhoto()
        }.flow.cachedIn(viewModelScope).debounce(5000)
    }
}