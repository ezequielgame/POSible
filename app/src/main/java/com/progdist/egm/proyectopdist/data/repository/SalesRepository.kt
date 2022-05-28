package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.SalesApi
import retrofit2.http.Field
import retrofit2.http.Query

class SalesRepository(
    private val api: SalesApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getBranch(
        where: String,
        id_branch: Int
    ) = safeApiCall {
        api.getBranch(where,id_branch)
    }

    suspend fun getItems(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getItems(where, idWhere)
    }

    suspend fun getPaymentTypes(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getPaymentTypes(where, idWhere)
    }

    suspend fun getCustomers(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getCustomers(where, idWhere)
    }

    suspend fun addOwnerSale(
        idBranchSale: Int,
        idCustomerSale: Int,
        idUserSale: Int,
        total: Float,
        quantity: Int,
        idPaymentSale: Int,
    ) = safeApiCall {
        api.addOwnerSale(idBranchSale, idCustomerSale, idUserSale, total,quantity, idPaymentSale)
    }

    suspend fun editItemStock(
        id: Int,
        nameId: String,
        stock: Int
    ) = safeApiCall {
        api.editItemStock(id, nameId, stock)
    }

    suspend fun addItemSale(
        idItem: Int,
        idSale: Int,
        quantity: Int,
        total: Float
    ) = safeApiCall {
        api.addItemSale(idItem, idSale, quantity, total)
    }

    suspend fun getSalesRange(
        where: String,
        between1: String,
        between2: String,
        filterIn: String,
        filterTo: String
    ) = safeApiCall {
        api.getSalesRange(where, between1, between2, filterIn, filterTo)
    }

    suspend fun getSalesItems(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getSalesItems(where, idWhere)
    }

}