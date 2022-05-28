package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetCategoriesResponse
import com.progdist.egm.proyectopdist.domain.EditCategory
import com.progdist.egm.proyectopdist.domain.GetCategories
import kotlinx.coroutines.launch

class CategorySummaryViewModel(
    private val repository: InventoryRepository
) : ViewModel(){

    private val _getCategoriesResponse: MutableLiveData<Resource<GetCategoriesResponse>> = MutableLiveData()
    val getCategoriesResponse: LiveData<Resource<GetCategoriesResponse>> get() = _getCategoriesResponse

    private val _editCategoryResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editCategoryResponse: LiveData<Resource<EditResponse>> get() = _editCategoryResponse

    fun getCategories(
        where: String,
        branchId: Int
    ) = viewModelScope.launch{
        val getCategoriesUseCase = GetCategories()
        _getCategoriesResponse.value = (getCategoriesUseCase(repository,where,branchId) as Resource<GetCategoriesResponse>)
    }

    fun editCategory(
        id: Int,
        nameId: String,
        name: String,
        description: String
    ) = viewModelScope.launch {
        val editCategoryUseCase = EditCategory()
       _editCategoryResponse.value = (editCategoryUseCase(repository,id,nameId, name, description) as Resource<EditResponse>)
    }


}