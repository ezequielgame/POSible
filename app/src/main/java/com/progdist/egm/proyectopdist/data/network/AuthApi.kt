package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("users")
    suspend fun login( //Call asynchronously
        @Field("action") action: String,
        @Field("email_user") email: String,
        @Field("password_user") password: String,
    ) : LoginResponse

}