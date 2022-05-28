package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetCategoriesResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetItemsResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetSuppliersResponse
import com.progdist.egm.proyectopdist.domain.EditItem
import com.progdist.egm.proyectopdist.domain.GetCategories
import com.progdist.egm.proyectopdist.domain.GetItems
import com.progdist.egm.proyectopdist.domain.GetSuppliers
import kotlinx.coroutines.launch

class ItemSummaryViewModel(
    private val repository: InventoryRepository
) : ViewModel(){

    private val _getItemsResponse: MutableLiveData<Resource<GetItemsResponse>> = MutableLiveData()
    val getItemsResponse: LiveData<Resource<GetItemsResponse>> get() = _getItemsResponse

    private val _getCategoriesResponse: MutableLiveData<Resource<GetCategoriesResponse>> = MutableLiveData()
    val getCategoriesResponse: LiveData<Resource<GetCategoriesResponse>> get() = _getCategoriesResponse

    private val _getSuppliersResponse: MutableLiveData<Resource<GetSuppliersResponse>> = MutableLiveData()
    val getSuppliersResponse: LiveData<Resource<GetSuppliersResponse>> get() = _getSuppliersResponse

    private val _editItemResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editItemResponse: LiveData<Resource<EditResponse>> get() = _editItemResponse

    fun getItems(
        where: String,
        idWhere: Int
    ) = viewModelScope.launch{
        val getItemsUseCase = GetItems()
        _getItemsResponse.value = (getItemsUseCase(repository,where,idWhere) as Resource<GetItemsResponse>)
    }

    fun getCategories(
        where: String,
        branchId: Int
    ) = viewModelScope.launch{
        val getCategoriesUseCase = GetCategories()
        _getCategoriesResponse.value = (getCategoriesUseCase(repository,where,branchId) as Resource<GetCategoriesResponse>)
    }

    fun getSuppliers(
        where: String,
        userId: Int
    ) = viewModelScope.launch{
        val getSuppliersUseCase = GetSuppliers()
        _getSuppliersResponse.value = (getSuppliersUseCase(repository,where,userId) as Resource<GetSuppliersResponse>)
    }

    fun editItem(
        id: Int,
        whereId: String,
        code: String,
        name: String,
        sale: Int,
        purchase: Int,
        stock: Int,
        idCategory: Int,
        idSupplier: Int
    ) = viewModelScope.launch {
        val editItemUseCase = EditItem()
        _editItemResponse.value = (editItemUseCase(repository,id,whereId,code, name, sale, purchase, stock, idCategory, idSupplier) as Resource<EditResponse>)
    }


}