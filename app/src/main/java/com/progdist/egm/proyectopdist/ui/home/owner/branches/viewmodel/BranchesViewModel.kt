package com.progdist.egm.proyectopdist.ui.home.owner.branches.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.domain.DeleteBranch
import com.progdist.egm.proyectopdist.domain.DeleteBranchLocation
import com.progdist.egm.proyectopdist.domain.GetBranchesList
import kotlinx.coroutines.launch

class BranchesViewModel(
    private val repository: BranchesRepository
) : ViewModel(){

    private val _getBranchesListResponse: MutableLiveData<Resource<GetBranchesListResponse>> = MutableLiveData()
    val getBranchesListResponse: LiveData<Resource<GetBranchesListResponse>> get() = _getBranchesListResponse

    private val _deleteBranchResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    val deleteBranchResponse: LiveData<Resource<DeleteResponse>> get() = _deleteBranchResponse

    private val _deleteBranchLocationResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    val deleteBranchLocationResponse: LiveData<Resource<DeleteResponse>> get() = _deleteBranchLocationResponse
    
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
        _deleteBranchResponse.value = (deleteBranchUseCase(repository,id,nameId) as Resource<DeleteResponse>)
    }


    fun deleteBranchLocation(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteBranchLocationUseCase = DeleteBranchLocation()
        _deleteBranchLocationResponse.value = (deleteBranchLocationUseCase(repository,id,nameId) as Resource<DeleteResponse>)
    }

    fun deleteIdWorkingBranch() = viewModelScope.launch {
        repository.deleteIdWorkingBranch()
    }




}