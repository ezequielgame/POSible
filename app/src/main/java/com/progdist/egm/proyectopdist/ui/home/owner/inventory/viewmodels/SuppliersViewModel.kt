package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.DeleteCategoryResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.DeleteSupplierResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetSuppliersResponse
import com.progdist.egm.proyectopdist.domain.DeleteCategory
import com.progdist.egm.proyectopdist.domain.DeleteSupplier
import com.progdist.egm.proyectopdist.domain.GetSuppliers
import kotlinx.coroutines.launch

class SuppliersViewModel(
    private val repository: InventoryRepository
) : ViewModel(){

    private val _getSuppliersResponse: MutableLiveData<Resource<GetSuppliersResponse>> = MutableLiveData()
    val getSuppliersResponse: LiveData<Resource<GetSuppliersResponse>> get() = _getSuppliersResponse

    private val _deleteSupplierResponse: MutableLiveData<Resource<DeleteSupplierResponse>> = MutableLiveData()
    val deleteSupplierResponse: LiveData<Resource<DeleteSupplierResponse>> get() = _deleteSupplierResponse

    fun getSuppliers(
        where: String,
        userId: Int
    ) = viewModelScope.launch{
        val getSuppliersUseCase = GetSuppliers()
        _getSuppliersResponse.value = (getSuppliersUseCase(repository,where,userId) as Resource<GetSuppliersResponse>)
    }

    fun deleteSupplier(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteSupplierUseCase = DeleteSupplier()
        _deleteSupplierResponse.value = (deleteSupplierUseCase(repository,id, nameId) as Resource<DeleteSupplierResponse>)
    }

}