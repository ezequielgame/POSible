package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.EditSupplierResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetSuppliersResponse
import com.progdist.egm.proyectopdist.domain.EditSupplier
import com.progdist.egm.proyectopdist.domain.GetSuppliers
import kotlinx.coroutines.launch

class SupplierSummaryViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _getSuppliersResponse: MutableLiveData<Resource<GetSuppliersResponse>> = MutableLiveData()
    val getSuppliersResponse: LiveData<Resource<GetSuppliersResponse>> get() = _getSuppliersResponse

    private val _editSupplierResponse: MutableLiveData<Resource<EditSupplierResponse>> = MutableLiveData()
    val editSupplierResponse: LiveData<Resource<EditSupplierResponse>> get() = _editSupplierResponse

    fun getSuppliers(
        where: String,
        userId: Int
    ) = viewModelScope.launch{
        val getSuppliersUseCase = GetSuppliers()
        _getSuppliersResponse.value = (getSuppliersUseCase(repository,where,userId) as Resource<GetSuppliersResponse>)
    }

    fun editSupplier(
        id: Int,
        nameId: String,
        name: String,
        phone: String,
        mail: String
    ) = viewModelScope.launch {

        val editSupplierUseCase = EditSupplier()
        _editSupplierResponse.value = (editSupplierUseCase(repository,id, nameId, name, phone, mail) as Resource<EditSupplierResponse>)

    }

}