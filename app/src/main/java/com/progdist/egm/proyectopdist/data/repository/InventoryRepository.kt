package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.InventoryApi
import retrofit2.http.Field
import retrofit2.http.Query

class InventoryRepository(
    private val api: InventoryApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun getBranch(
        where: String,
        id_branch: Int
    ) = safeApiCall {
        api.getBranch(where,id_branch)
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

    suspend fun getItems(
        where: String,
        idWhere: Int
    ) = safeApiCall {
        api.getItems(where, idWhere)
    }

    suspend fun addItem(
        idBranchItem: Int,
        code: String,
        name: String,
        sale: Float,
        purchase: Float,
        stock: Int,
        idCategory: Int,
        idSupplier: Int
    ) = safeApiCall {
        api.addItem(idBranchItem, code, name, sale, purchase,stock, idCategory, idSupplier)
    }

    suspend fun editItem(
        id: Int,
        nameId: String,
        code: String,
        name: String,
        sale: Int,
        purchase: Int,
        stock: Int,
        idCategory: Int,
        idSupplier: Int
    ) = safeApiCall {
        api.editItem(id,nameId, code, name, sale, purchase, stock, idCategory, idSupplier)
    }


}