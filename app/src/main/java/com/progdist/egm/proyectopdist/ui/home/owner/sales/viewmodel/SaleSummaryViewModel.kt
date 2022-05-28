package com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.sales.GetCustomersResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetPaymentTypesResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetSalesItemsResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetSalesResponse
import com.progdist.egm.proyectopdist.domain.GetCustomers
import com.progdist.egm.proyectopdist.domain.GetPaymentTypes
import com.progdist.egm.proyectopdist.domain.GetSales
import com.progdist.egm.proyectopdist.domain.GetSalesItems
import kotlinx.coroutines.launch

class SaleSummaryViewModel(
    private val repository: SalesRepository
) : ViewModel() {

    private val _getSalesItemsResponse: MutableLiveData<Resource<GetSalesItemsResponse>> = MutableLiveData()
    val getSalesItemsResponse: LiveData<Resource<GetSalesItemsResponse>> get() = _getSalesItemsResponse

    private val _getPaymentTypesResponse: MutableLiveData<Resource<GetPaymentTypesResponse>> = MutableLiveData()
    val getPaymentTypesResponse: LiveData<Resource<GetPaymentTypesResponse>> get() = _getPaymentTypesResponse

    private val _getCustomersResponse: MutableLiveData<Resource<GetCustomersResponse>> = MutableLiveData()
    val getCustomersResponse: LiveData<Resource<GetCustomersResponse>> get() = _getCustomersResponse

    fun getSalesItems(
        where: String,
        idWhere: String
    ) = viewModelScope.launch {
        val getSalesItemsUseCase = GetSalesItems()
        _getSalesItemsResponse.value = (getSalesItemsUseCase(repository,where,idWhere) as Resource<GetSalesItemsResponse>)
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

}