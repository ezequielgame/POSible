package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.domain.AddSupplier
import kotlinx.coroutines.launch

class AddSupplierViewModel(
    private val repository: InventoryRepository
): ViewModel() {


    private val _addSupplierResponse : MutableLiveData<Resource<AddResponse>> = MutableLiveData() //Put the value
    val addSupplierResponse: LiveData<Resource<AddResponse>> get() = _addSupplierResponse


    fun addSupplier(
        idUserSupplier: Int,
        name: String,
        phone: String,
        mail: String
    ) = viewModelScope.launch {
        val addSupplierUseCase = AddSupplier()
        _addSupplierResponse.value = (addSupplierUseCase(repository,idUserSupplier,name,phone,mail) as Resource<AddResponse>)
    }

}