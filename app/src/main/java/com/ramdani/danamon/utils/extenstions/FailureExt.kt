package com.ramdani.danamon.utils.extenstions

import com.google.gson.Gson
import com.ramdani.danamon.utils.Failure
import com.ramdani.danamon.utils.model.GeneralResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.generalErrorServer(): Failure {
    this.printStackTrace()
    return when (this) {
        is HttpException -> {
            try {
                val response = Gson().fromJson(
                    this.response()?.errorBody()?.charStream(),
                    GeneralResponse::class.java
                )

                when {
                    this.code() == 500 -> {
                        return Failure.ServerError("Terjadi kesalahan server, mohon coba kembali nanti")
                    }

                    this.code() == 401 -> {
                        return Failure.ExpiredSession
                    }

                    this.code() == 413 -> {
                        return Failure.DataNotExist("Ukuran file/input terlalu besar, mohon input kembali")
                    }

                    this.code() == 202 -> {
                        return Failure.DataNotExist(response.message?.id.orEmpty())
                    }

                    else -> Failure.ServerError((response.message?.id.orEmpty()))
                }

            } catch (e: Exception) {
                Failure.ServerError("Terjadi kesalahan server, mohon coba kembali nanti")
            }
        }

        is SocketTimeoutException -> Failure.ServerError("Waktu tunggu berakhir, mohon coba kembali")
        is UnknownHostException -> Failure.NetworkConnection
        is IOException -> Failure.DataNotExist("Terjadi kesalahan input/output dari server, silahkan input kembali nanti")
        else -> Failure.ServerError("Terjadi kesalahan, mohon coba kembali nanti")
    }
}