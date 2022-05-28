package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetCategoriesResponse
import com.progdist.egm.proyectopdist.domain.DeleteCategory
import com.progdist.egm.proyectopdist.domain.GetCategories
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _getCategoriesResponse: MutableLiveData<Resource<GetCategoriesResponse>> = MutableLiveData()
    val getCategoriesResponse: LiveData<Resource<GetCategoriesResponse>> get() = _getCategoriesResponse


    private val _deleteCategoryResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    val deleteCategoryResponse: LiveData<Resource<DeleteResponse>> get() = _deleteCategoryResponse

    fun getCategories(
        where: String,
        branchId: Int
    ) = viewModelScope.launch{
        val getCategoriesUseCase = GetCategories()
        _getCategoriesResponse.value = (getCategoriesUseCase(repository,where,branchId) as Resource<GetCategoriesResponse>)
    }

    fun deleteCategory(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteCategoryUseCase = DeleteCategory()
        _deleteCategoryResponse.value = (deleteCategoryUseCase(repository,id, nameId) as Resource<DeleteResponse>)
    }

}