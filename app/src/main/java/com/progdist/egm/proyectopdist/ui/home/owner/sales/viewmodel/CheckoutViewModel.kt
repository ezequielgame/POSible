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

    private val _addOwnerSaleResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addOwnerSaleResponse: LiveData<Resource<AddResponse>> get() = _addOwnerSaleResponse

    private val _addSaleItemResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addSaleItemResponse: LiveData<Resource<AddResponse>> get() = _addSaleItemResponse

    private val _editItemStockResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editItemStockResponse: LiveData<Resource<EditResponse>> get() = _editItemStockResponse

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

    fun addOwnerSale(
        idBranchSale: Int,
        idCustomerSale: Int,
        idUserSale: Int,
        total: Float,
        quantity: Int,
        idPaymentSale: Int
    ) = viewModelScope.launch {
        val addOwnerSaleUseCase = AddSale()
        _addOwnerSaleResponse.value = (addOwnerSaleUseCase(repository,idBranchSale,idCustomerSale,idUserSale,total,quantity,idPaymentSale) as Resource<AddResponse>)
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

    fun editItemStock(
        id: Int,
        nameId: String,
        stock: Int
    ) = viewModelScope.launch {
        val editItemStockUseCase = EditItemStock()
        _editItemStockResponse.value = (editItemStockUseCase(repository, id, nameId, stock) as Resource<EditResponse>)
    }

}