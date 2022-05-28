package com.progdist.egm.proyectopdist.ui.home.owner.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.domain.GetBranch
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _getBranchesListResponse: MutableLiveData<Resource<GetBranchesListResponse>> = MutableLiveData()
    val getBranchesListResponse: LiveData<Resource<GetBranchesListResponse>> get() = _getBranchesListResponse

    fun getBranchInInventory(
        where:String,
        id_branch: Int
    ) = viewModelScope.launch {
        val getBranchInInventoryUseCase = GetBranch()
        _getBranchesListResponse.value = (getBranchInInventoryUseCase(repository,where,id_branch) as Resource<GetBranchesListResponse>)

    }

}