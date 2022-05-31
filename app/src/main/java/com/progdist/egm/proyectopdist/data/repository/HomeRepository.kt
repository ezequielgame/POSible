package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.HomeApi
import retrofit2.http.Query

class HomeRepository(
    private val api: HomeApi,
    private val preferences: UserPreferences
) : BaseRepository() {


    suspend fun getUser(
        id_user: Int
    ) = safeApiCall {

        api.getUser("id_user", id_user)

    }

    suspend fun getBranch(
        idBranch: Int
    ) = safeApiCall {
        api.getBranch("id_branch",idBranch)
    }

    suspend fun saveIdSelectedBranch(branchId: Int){
        preferences.saveIdWorkingBranch(branchId)
    }

    suspend fun deleteAuthToken(){
        preferences.deleteAuthToken()
    }

    suspend fun getEmployees(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getEmployees(where, idWhere)
    }

}