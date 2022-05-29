package com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetItemsResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetSuppliersResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetCustomersResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetPaymentTypesResponse
import com.progdist.egm.proyectopdist.domain.*
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: SalesRepository
) : ViewModel() {

    private val _getItemsResponse: MutableLiveData<Resource<GetItemsResponse>> = MutableLiveData()
    val getItemsResponse: LiveData<Resource<GetItemsResponse>> get() = _getItemsResponse

    private val _getPaymentTypesResponse: MutableLiveData<Resource<GetPaymentTypesResponse>> = MutableLiveData()
    val getPaymentTypesResponse: LiveData<Resource<GetPaymentTypesResponse>> get() = _getPaymentTypesResponse

    private val _getCustomersResponse: MutableLiveData<Resource<GetCustomersResponse>> = MutableLiveData()
    val getCustomersResponse: LiveData<Resource<GetCustomersResponse>> get() = _getCustomersResponse

    private val _addSaleResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addSaleResponse: LiveData<Resource<AddResponse>> get() = _addSaleResponse

    private val _addPurchaseResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addPurchaseResponse: LiveData<Resource<AddResponse>> get() = _addPurchaseResponse

    private val _addSaleItemResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addSaleItemResponse: LiveData<Resource<AddResponse>> get() = _addSaleItemResponse

    private val _addPurchaseItemResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addPurchaseItemResponse: LiveData<Resource<AddResponse>> get() = _addPurchaseItemResponse

    private val _editItemStockResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editItemStockResponse: LiveData<Resource<EditResponse>> get() = _editItemStockResponse

    private val _getSuppliersResponse: MutableLiveData<Resource<GetSuppliersResponse>> = MutableLiveData()
    val getSuppliersResponse: LiveData<Resource<GetSuppliersResponse>> get() = _getSuppliersResponse

    fun getItems(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getItemsUseCase = GetItems()
        _getItemsResponse.value = (getItemsUseCase(repository,where,idWhere) as Resource<GetItemsResponse>)
    }

    fun getPaymentTypes(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getPaymentTypesUseCase = GetPaymentTypes()
        _getPaymentTypesResponse.value = (getPaymentTypesUseCase(repository,where,idWhere) as Resource<GetPaymentTypesResponse>)
    }

    fun getCustomers(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getCustomersUseCase = GetCustomers()
        _getCustomersResponse.value = (getCustomersUseCase(repository,where,idWhere) as Resource<GetCustomersResponse>)
    }

    fun addSale(
        idBranchSale: Int,
        idCustomerSale: Int,
        idUserSale: Int,
        total: Float,
        quantity: Int,
        idPaymentSale: Int,
        idEmployeeSale: Int? = 1
    ) = viewModelScope.launch {
        val addSaleUseCase = AddSale()
        _addSaleResponse.value = (addSaleUseCase(repository,idBranchSale,idCustomerSale,idUserSale,total,quantity,idPaymentSale,idEmployeeSale) as Resource<AddResponse>)
    }

    fun addPurchase(
        idBranchPurchase: Int,
        idCustomerPurchase: Int,
        idUserPurchase: Int,
        total: Float,
        quantity: Int,
        idPaymentPurchase: Int,
        idEmployeePurchase: Int? = 1
    ) = viewModelScope.launch {
        val addPurchaseUseCase = AddPurchase()
        _addPurchaseResponse.value = (addPurchaseUseCase(repository,idBranchPurchase,idCustomerPurchase,idUserPurchase,total,quantity,idPaymentPurchase,idEmployeePurchase) as Resource<AddResponse>)
    }

    fun addSaleItem(
        idItem: Int,
        idSale: Int,
        quantity: Int,
        total: Float,
    ) = viewModelScope.launch {
        val addSaleItemUseCase = AddSaleItem()
        _addSaleItemResponse.value = (addSaleItemUseCase(repository,idItem, idSale, quantity, total) as Resource<AddResponse>)
    }

    fun addPurchaseItem(
        idItem: Int,
        idPurchase: Int,
        quantity: Int,
        total: Float,
    ) = viewModelScope.launch {
        val addPurchaseItemUseCase = AddPurchaseItem()
        _addPurchaseItemResponse.value = (addPurchaseItemUseCase(repository,idItem, idPurchase, quantity, total) as Resource<AddResponse>)
    }

    fun editItemStock(
        id: Int,
        nameId: String,
        stock: Int
    ) = viewModelScope.launch {
        val editItemStockUseCase = EditItemStock()
        _editItemStockResponse.value = (editItemStockUseCase(repository, id, nameId, stock) as Resource<EditResponse>)
    }

    fun getSuppliers(
        where: String,
        userId: Int
    ) = viewModelScope.launch{
        val getSuppliersUseCase = GetSuppliers()
        _getSuppliersResponse.value = (getSuppliersUseCase(repository,where,userId) as Resource<GetSuppliersResponse>)
    }

}