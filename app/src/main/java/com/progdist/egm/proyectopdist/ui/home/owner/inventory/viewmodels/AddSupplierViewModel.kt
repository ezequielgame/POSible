package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.inventory.AddSupplierResponse
import com.progdist.egm.proyectopdist.data.responses.locations.AddLocationResponse
import com.progdist.egm.proyectopdist.domain.AddCategory
import com.progdist.egm.proyectopdist.domain.AddSupplier
import kotlinx.coroutines.launch

class AddSupplierViewModel(
    private val repository: InventoryRepository
): ViewModel() {


    private val _addSupplierResponse : MutableLiveData<Resource<AddSupplierResponse>> = MutableLiveData() //Put the value
    val addSupplierResponse: LiveData<Resource<AddSupplierResponse>> get() = _addSupplierResponse


    fun addSupplier(
        idUserSupplier: Int,
        name: String,
        phone: String,
        mail: String
    ) = viewModelScope.launch {
        val addSupplierUseCase = AddSupplier()
        _addSupplierResponse.value = (addSupplierUseCase(repository,idUserSupplier,name,phone,mail) as Resource<AddSupplierResponse>)
    }

}