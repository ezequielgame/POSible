package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.BranchesApi
import com.progdist.egm.proyectopdist.data.network.UploadBranchImageRequestBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import java.io.File

class BranchesRepository(
    private val api: BranchesApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun addBranchLocation(
        address: String,
        city: String,
        estate: String,
        country: String
    ) = safeApiCall {
        api.addBranchLocation(address,city,estate,country)
    }

    suspend fun addBranch(
        id_user: String,
        name: String,
        id_location: String,
        phone: String,
        description: String,
    ) = safeApiCall {
        api.addBranch(id_user,name, id_location, phone, description)
    }

    suspend fun getBranchesList(
        where: String,
        id_user: String
    ) = safeApiCall {
        api.getBranchesList(where,id_user)
    }

    suspend fun getBranch(
        where: String,
        id_branch: Int
    ) = safeApiCall {
        api.getBranch(where, id_branch)
    }

    suspend fun editBranchLocation(
        id: Int,
        nameId: String,
        address: String,
        city: String,
        estate: String,
        country: String
    ) = safeApiCall {
        api.editBranchLocation(id,nameId,address, city, estate, country)
    }

    suspend fun editBranch(
        id: Int,
        nameId: String,
        name: String,
        phone: String,
        description: String,
    ) = safeApiCall {
        api.editBranch(id, nameId, name, phone, description)
    }

    suspend fun deleteBranch(
        id: Int,
        nameId: String
    ) = safeApiCall {
        api.deleteBranch(id,nameId)
    }

    suspend fun deleteBranchLocation(
        id: Int,
        nameId: String
    ) = safeApiCall {
        api.deleteBranchLocation(id,nameId)
    }

    suspend fun deleteIdWorkingBranch(){
        preferences.deleteIdWorkingBranch()
    }

}