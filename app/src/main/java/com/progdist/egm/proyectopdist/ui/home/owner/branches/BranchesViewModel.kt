package com.progdist.egm.proyectopdist.ui.home.owner.branches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.DeleteBranchLocationResponse
import com.progdist.egm.proyectopdist.data.responses.branches.DeleteBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.domain.DeleteBranch
import com.progdist.egm.proyectopdist.domain.DeleteBranchLocation
import com.progdist.egm.proyectopdist.domain.GetBranchesList
import kotlinx.coroutines.launch

class BranchesViewModel(
    private val repository: BranchesRepository
) : ViewModel(){

    private val _getBranchesListResponse: MutableLiveData<Resource<GetBranchesListResponse>> = MutableLiveData()
    val getBranchesListResponse: LiveData<Resource<GetBranchesListResponse>> get() = _getBranchesListResponse

    private val _deleteBranchResponse: MutableLiveData<Resource<DeleteBranchResponse>> = MutableLiveData()
    val deleteBranchResponse: LiveData<Resource<DeleteBranchResponse>> get() = _deleteBranchResponse

    private val _deleteBranchLocationResponse: MutableLiveData<Resource<DeleteBranchLocationResponse>> = MutableLiveData()
    val deleteBranchLocationResponse: LiveData<Resource<DeleteBranchLocationResponse>> get() = _deleteBranchLocationResponse
    
    fun getBranchesList(
        where: String,
        id_user: String
    ) = viewModelScope.launch { 
        val getBranchesListUseCase = GetBranchesList()
        _getBranchesListResponse.value = (getBranchesListUseCase(repository, where, id_user) as Resource<GetBranchesListResponse>?)
        
    }

    fun deleteBranch(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteBranchUseCase = DeleteBranch()
        _deleteBranchResponse.value = (deleteBranchUseCase(repository,id,nameId) as Resource<DeleteBranchResponse>)
    }


    fun deleteBranchLocation(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteBranchLocationUseCase = DeleteBranchLocation()
        _deleteBranchLocationResponse.value = (deleteBranchLocationUseCase(repository,id,nameId) as Resource<DeleteBranchLocationResponse>)
    }

    fun deleteIdWorkingBranch() = viewModelScope.launch {
        repository.deleteIdWorkingBranch()
    }




}