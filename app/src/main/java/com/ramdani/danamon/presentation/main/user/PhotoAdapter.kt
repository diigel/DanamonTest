package com.ramdani.danamon.presentation.main.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramdani.danamon.core.extenstions.loadImage
import com.ramdani.danamon.data.response.ResponseDataPhoto
import com.ramdani.danamon.databinding.ItemListPhotoBinding

class PhotoAdapter : PagingDataAdapter<ResponseDataPhoto, PhotoAdapter.PhotoViewHolder>(PhotoComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemListPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoItem = getItem(position)
        photoItem?.let { holder.bind(it) }
    }

    inner class PhotoViewHolder(private val binding: ItemListPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: ResponseDataPhoto) {
            binding.run {
                imgPhoto.loadImage(photo.url)
                imgBackground.loadImage(photo.thumbnailUrl)
                val title = StringBuilder()
                    .append(photo.id)
                    .append(" ")
                    .append(photo.title)
                txtTitle.text = title
            }
        }
    }

    object PhotoComparator : DiffUtil.ItemCallback<ResponseDataPhoto>() {
        override fun areItemsTheSame(oldItem: ResponseDataPhoto, newItem: ResponseDataPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResponseDataPhoto, newItem: ResponseDataPhoto): Boolean {
            return oldItem == newItem
        }
    }
}