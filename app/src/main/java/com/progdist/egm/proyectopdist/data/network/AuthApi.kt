package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.auth.EmployeeLoginResponse
import com.progdist.egm.proyectopdist.data.responses.auth.LoginResponse
import com.progdist.egm.proyectopdist.data.responses.auth.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @FormUrlEncoded
    @POST("users")
    suspend fun login( //Call asynchronously
        @Query("action") action: String,
        @Field("email_user") email: String,
        @Field("password_user") password: String,
    ) : LoginResponse

    @FormUrlEncoded
    @POST("users")
    suspend fun register(
        @Query("action") action: String,
        @Field("email_user") email: String,
        @Field("password_user") password: String,
        @Field("business_name_user") business: String
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("employees")
    suspend fun employeeLogin( //Call asynchronously
        @Query("action") action: String,
        @Field("email_employee") email: String,
        @Field("password_employee") password: String,
        @Field("suffix") suffix: String? = "employee",
    ) : EmployeeLoginResponse

}