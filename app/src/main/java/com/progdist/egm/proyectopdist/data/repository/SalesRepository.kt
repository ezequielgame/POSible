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
        api.getBranch(where, id_branch)
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

    suspend fun addSale(
        idBranchSale: Int,
        idCustomerSale: Int,
        idUserSale: Int,
        total: Float,
        quantity: Int,
        idPaymentSale: Int,
        idEmployeeSale: Int? = 1
    ) = safeApiCall {
        api.addSale(idBranchSale, idCustomerSale, idUserSale, total, quantity, idPaymentSale,idEmployeeSale)
    }

    suspend fun addPurchase(
        idBranchPurchase: Int,
        idCustomerPurchase: Int,
        idUserPurchase: Int,
        total: Float,
        quantity: Int,
        idPaymentPurchase: Int,
        idEmployeePurchase: Int? = 1
    ) = safeApiCall {
        api.addPurchase(idBranchPurchase, idCustomerPurchase, idUserPurchase, total, quantity, idPaymentPurchase,idEmployeePurchase)
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

    suspend fun addItemPurchase(
        idItem: Int,
        idPurchase: Int,
        quantity: Int,
        total: Float
    ) = safeApiCall {
        api.addItemPurchase(idItem, idPurchase, quantity, total)
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

    suspend fun getPurchasesRange(
        where: String,
        between1: String,
        between2: String,
        filterIn: String,
        filterTo: String
    ) = safeApiCall {
        api.getPurchasesRange(where, between1, between2, filterIn, filterTo)
    }

    suspend fun getSalesItems(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getSalesItems(where, idWhere)
    }

    suspend fun getPurchasesItems(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getPurchasesItems(where, idWhere)
    }

    suspend fun getSuppliers(
        where: String,
        userId: Int
    ) = safeApiCall {
        api.getSuppliers(where,userId)
    }

}