package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    //Safely call the Api
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T>{
        //Execute Api Call
        return withContext(Dispatchers.IO){
            try {
                Resource.success(apiCall.invoke())
            } catch(throwable: Throwable){
                when (throwable){
                    is HttpException -> {
                        Resource.failure(false, throwable.code(), throwable.response()?.errorBody())
                    } else -> {
                        Resource.failure(true, null, null)
                    }
                }
            }
        }
    }

}