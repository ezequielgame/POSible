package com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.GetItemsResponse
import com.progdist.egm.proyectopdist.domain.GetItems
import kotlinx.coroutines.launch

class NewSaleViewModel(
    private val repository: SalesRepository
) : ViewModel() {

    private val _getItemsResponse: MutableLiveData<Resource<GetItemsResponse>> = MutableLiveData()
    val getItemsResponse: LiveData<Resource<GetItemsResponse>> get() = _getItemsResponse

    fun getItems(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getItemsUseCase = GetItems()
        _getItemsResponse.value = (getItemsUseCase(repository,where,idWhere) as Resource<GetItemsResponse>)
    }



}