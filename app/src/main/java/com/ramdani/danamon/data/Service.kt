package com.ramdani.danamon.data

import com.ramdani.danamon.base.Network
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    companion object {

        fun createService(): Service {
            return Network.build().create(Service::class.java)
        }
    }

    @GET("photos?")
    fun getDataHome(
        @Query("page") pageNumber: String? = null,
        @Query("limit") limit: String? = null,
    ) : Flowable<List<ResponseDataHome>>

}