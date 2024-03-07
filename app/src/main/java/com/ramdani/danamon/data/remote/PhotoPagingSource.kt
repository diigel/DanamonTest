package com.ramdani.danamon.data.remote

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.ramdani.danamon.data.Service
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PhotoPagingSource(private val service: Service) : RxPagingSource<Int,ResponseDataPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, ResponseDataPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ResponseDataPhoto>> {
        val page = params.key ?: 1
        return service.getDataPhoto(page, 10)
            .subscribeOn(Schedulers.io())
            .map { data ->
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                ) as LoadResult<Int,ResponseDataPhoto>
            }
            .onErrorReturn { e ->
                LoadResult.Error(e)
            }
    }
}