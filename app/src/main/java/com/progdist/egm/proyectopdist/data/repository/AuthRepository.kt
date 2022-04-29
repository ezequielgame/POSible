package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.AuthApi

class AuthRepository(
    private val api:AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
        domain: String,
        action: String,
        email: String,
        password: String
    ) = safeApiCall {
        when(domain){
            "employees" ->{
                api.employeeLogin(action, email, password)
            }
            else -> {
                api.login(action, email, password)
            }
        }
    }

    suspend fun register(
        action: String,
        email: String,
        password: String,
        business_name: String
    ) = safeApiCall {
        api.register(action,email,password,business_name)
    }


    suspend fun saveAuthToken(token: String){
        preferences.saveAuthToken(token)
    }

}