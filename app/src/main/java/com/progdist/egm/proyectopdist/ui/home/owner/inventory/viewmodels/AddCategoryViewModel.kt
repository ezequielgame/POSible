package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.locations.AddLocationResponse
import com.progdist.egm.proyectopdist.domain.AddCategory
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val repository: InventoryRepository
) : ViewModel(){



    private val _addCategoryResponse : MutableLiveData<Resource<AddLocationResponse>> = MutableLiveData() //Put the value
    val addCategoryResponse: LiveData<Resource<AddLocationResponse>> get() = _addCategoryResponse


    fun addCategory(
        idUser: Int,
        name: String,
        description: String
    ) = viewModelScope.launch {
        val addCategoryUseCase = AddCategory()
        _addCategoryResponse.value = (addCategoryUseCase(repository,idUser,name, description) as Resource<AddLocationResponse>)
    }


}