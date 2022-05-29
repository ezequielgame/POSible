package com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.sales.GetPurchasesResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetSalesResponse
import com.progdist.egm.proyectopdist.domain.GetPurchases
import com.progdist.egm.proyectopdist.domain.GetSales
import kotlinx.coroutines.launch

class SalesListViewModel(
    private val repository: SalesRepository
) : ViewModel() {


    private val _getSalesRangeResponse: MutableLiveData<Resource<GetSalesResponse>> = MutableLiveData()
    val getSalesResponse: LiveData<Resource<GetSalesResponse>> get() = _getSalesRangeResponse

    fun getSalesRange(
        where: String,
        between1: String,
        between2: String,
        filterIn: String,
        filterTo: String
    ) = viewModelScope.launch {
        val getSalesRangeUseCase = GetSales()
        _getSalesRangeResponse.value = (getSalesRangeUseCase(repository,where, between1, between2, filterIn, filterTo) as Resource<GetSalesResponse>)
    }

    private val _getPurchasesRangeResponse: MutableLiveData<Resource<GetPurchasesResponse>> = MutableLiveData()
    val getPurchasesResponse: LiveData<Resource<GetPurchasesResponse>> get() = _getPurchasesRangeResponse

    fun getPurchasesRange(
        where: String,
        between1: String,
        between2: String,
        filterIn: String,
        filterTo: String
    ) = viewModelScope.launch {
        val getPurchasesRangeUseCase = GetPurchases()
        _getPurchasesRangeResponse.value = (getPurchasesRangeUseCase(repository,where, between1, between2, filterIn, filterTo) as Resource<GetPurchasesResponse>)
    }
    
    



}