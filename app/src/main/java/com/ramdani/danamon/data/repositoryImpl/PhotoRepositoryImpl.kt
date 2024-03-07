package com.ramdani.danamon.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.ramdani.danamon.data.Service
import com.ramdani.danamon.data.remote.PhotoPagingSource
import com.ramdani.danamon.data.repository.PhotoRepository
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Flowable

class PhotoRepositoryImpl(private val service: Service) : PhotoRepository {
    override fun getDataPhoto(): Flowable<PagingData<ResponseDataPhoto>> {
        val config = PagingConfig(pageSize = 10,
            enablePlaceholders = true,
            maxSize = 20,
            prefetchDistance = 5)
        return Pager(
            config = config,
            pagingSourceFactory = { PhotoPagingSource(service) }
        ).flowable
    }
}