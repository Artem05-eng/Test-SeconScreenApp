package com.example.test_app.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test_app.R
import com.example.test_app.SingleLiveEvent
import com.example.test_app.data.Photo
import com.example.test_app.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*

class PagingPhotoAdapter(
    private val mistake: SingleLiveEvent<Throwable?>,
    private val onItemClicked: (position: Int) -> Unit
): PagingDataAdapter<Photo, PagingPhotoAdapter.PhotoHolder>(
    DiffUtilCallbackPhoto()
) {

    class PhotoHolder(
        private val mistake: SingleLiveEvent<Throwable?>,
        override val containerView: View,
        private val onItemClicked: (position: Int) -> Unit
    ): RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        init {
            containerView!!.setOnClickListener {
                try {
                    onItemClicked(absoluteAdapterPosition)
                } catch (t: Throwable) {
                    mistake.postValue(t)
                }
            }
        }
        fun onBind(data: Photo) {
            Glide.with(itemView)
                .load(data.src.medium)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(imageCardCollection)
        }

    }
    override fun onBindViewHolder(holder: PagingPhotoAdapter.PhotoHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagingPhotoAdapter.PhotoHolder {
        return PagingPhotoAdapter.PhotoHolder(
            mistake,
            parent.inflate(R.layout.item),
            onItemClicked
        )
    }

}

class DiffUtilCallbackPhoto : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.alt == newItem.alt &&
                oldItem.photographer == newItem.photographer &&
                oldItem.src.original == newItem.src.original
    }

}