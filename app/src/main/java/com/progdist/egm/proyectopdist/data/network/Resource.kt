package com.progdist.egm.proyectopdist.data.network

import okhttp3.ResponseBody

sealed class Resource<out T> {

    data class success<out T>(val value: T) : Resource<T>()
    data class failure(
        val isNetworkError: Boolean?,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()

}