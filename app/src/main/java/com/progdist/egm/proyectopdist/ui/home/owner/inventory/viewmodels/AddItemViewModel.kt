package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetCategoriesResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetSuppliersResponse
import com.progdist.egm.proyectopdist.domain.AddItem
import com.progdist.egm.proyectopdist.domain.GetCategories
import com.progdist.egm.proyectopdist.domain.GetSuppliers
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val repository: InventoryRepository
) : ViewModel(){

    private val _getCategoriesResponse: MutableLiveData<Resource<GetCategoriesResponse>> = MutableLiveData()
    val getCategoriesResponse: LiveData<Resource<GetCategoriesResponse>> get() = _getCategoriesResponse

    private val _getSuppliersResponse: MutableLiveData<Resource<GetSuppliersResponse>> = MutableLiveData()
    val getSuppliersResponse: LiveData<Resource<GetSuppliersResponse>> get() = _getSuppliersResponse

    private val _addItemResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addItemResponse: LiveData<Resource<AddResponse>> get() = _addItemResponse

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

    fun addItem(
        idBranchItem: Int,
        code: String,
        name: String,
        sale: Float,
        purchase: Float,
        stock: Int,
        idCategory: Int,
        idSupplier: Int
    ) = viewModelScope.launch {
        val addItemUseCase = AddItem()
        _addItemResponse.value = (addItemUseCase(repository, idBranchItem, code, name, sale, purchase,stock, idCategory, idSupplier) as Resource<AddResponse>)

    }

}