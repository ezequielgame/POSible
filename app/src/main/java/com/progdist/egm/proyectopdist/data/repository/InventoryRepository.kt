package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import retrofit2.http.Field
import retrofit2.http.Query

class InventoryRepository(
    private val api: InventoryApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun getBranchInInventory(
        where: String,
        id_branch: Int
    ) = safeApiCall {
        api.getBranchInInventory(where,id_branch)
    }

    suspend fun getCategories(
        where: String,
        branchId: Int
    ) = safeApiCall {
        api.getCategories(where,branchId)
    }

    suspend fun getSuppliers(
        where: String,
        userId: Int
    ) = safeApiCall {
        api.getSuppliers(where,userId)
    }

    suspend fun addCategory(
        idUser: Int,
        name: String,
        description: String
    ) = safeApiCall {
        api.addCategory(idUser,name, description)
    }

    suspend fun editCategory(
        id:Int,
        nameId:String,
        name: String,
        description: String
    ) = safeApiCall {
        api.editCategory(id,nameId,name,description)
    }

    suspend fun editSupplier(
        id: Int,
        nameId: String,
        name: String,
        phone: String,
        mail: String
    ) = safeApiCall {
        api.editSupplier(id,nameId, name, phone, mail)
    }

    suspend fun deleteCategory(
        id: Int,
        nameId: String
    ) = safeApiCall {
        api.deleteCategory(id, nameId)
    }

    suspend fun deleteSupplier(
        id: Int,
        nameId: String
    ) = safeApiCall {
        api.deleteSupplier(id,nameId)
    }

    suspend fun addSupplier(
        idUserSupplier: Int,
        name: String,
        phone: String,
        mail: String
    ) = safeApiCall {
        api.addSupplier(idUserSupplier, name, phone, mail)
    }


}