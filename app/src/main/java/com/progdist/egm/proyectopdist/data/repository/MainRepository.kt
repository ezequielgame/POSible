package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.MainApi

class MainRepository(
    private val api: MainApi,
): BaseRepository(){

    suspend fun validateOwnerToken(

    ) = safeApiCall {
        api.ownerValidToken("authToken","user")
    }

    suspend fun validateEmployeeToken(

    ) = safeApiCall {
        api.employeeValidToken("authToken","employee")
    }

}