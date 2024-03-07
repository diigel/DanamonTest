package com.ramdani.danamon.data

import com.ramdani.danamon.core.base.Network
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    companion object {

        fun createService(): Service {
            return Network.build().create(Service::class.java)
        }
    }

    @GET("photos?")
    fun getDataPhoto(
        @Query("page") pageNumber: Int? = null,
        @Query("limit") limit: Int? = null,
    ) : Single<List<ResponseDataPhoto>>

}