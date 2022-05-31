package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetCategoriesResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetItemsResponse
import com.progdist.egm.proyectopdist.domain.DeleteCategory
import com.progdist.egm.proyectopdist.domain.DeleteItem
import com.progdist.egm.proyectopdist.domain.GetCategories
import com.progdist.egm.proyectopdist.domain.GetItems
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val repository: InventoryRepository
) : ViewModel(){


    private val _getItemsResponse: MutableLiveData<Resource<GetItemsResponse>> = MutableLiveData()
    val getItemsResponse: LiveData<Resource<GetItemsResponse>> get() = _getItemsResponse

    private val _deleteItemResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    val deleteItemResponse: LiveData<Resource<DeleteResponse>> get() = _deleteItemResponse

    fun getItems(
        where: String,
        idWhere: Int
    ) = viewModelScope.launch{
        val getItemsUseCase = GetItems()
        _getItemsResponse.value = (getItemsUseCase(repository,where,idWhere) as Resource<GetItemsResponse>)
    }

    fun deleteItem(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteItemUseCase = DeleteItem()
        _deleteItemResponse.value = (deleteItemUseCase(repository,id, nameId) as Resource<DeleteResponse>)
    }

}