package com.ramdani.danamon.data.repository

import androidx.paging.PagingData
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Flowable

interface PhotoRepository {

    fun getDataPhoto() : Flowable<PagingData<ResponseDataPhoto>>
}