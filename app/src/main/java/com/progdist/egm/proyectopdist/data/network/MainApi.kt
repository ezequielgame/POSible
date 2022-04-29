package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.main.ValidateEmployeeTokenResponse
import com.progdist.egm.proyectopdist.data.responses.main.ValidateOwnerTokenResponse
import retrofit2.http.*

interface MainApi {

    @POST("users")
    suspend fun ownerValidToken( //Call asynchronously
        @Query("action") action: String,
        @Query("suffix") suffix: String
    ) : ValidateOwnerTokenResponse


    @POST("employees")
    suspend fun employeeValidToken( //Call asynchronously
        @Query("action") action: String,
        @Query("suffix") suffix: String
    ) : ValidateEmployeeTokenResponse

}