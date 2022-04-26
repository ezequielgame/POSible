package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.network.AuthApi

class AuthRepository(
    private val api:AuthApi
) : BaseRepository(){

    suspend fun login(
        action: String,
        email: String,
        password: String
    ) = safeApiCall {
        api.login(action, email, password)
    }

}