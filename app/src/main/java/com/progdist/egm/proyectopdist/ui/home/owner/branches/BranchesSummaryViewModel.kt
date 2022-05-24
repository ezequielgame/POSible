package com.progdist.egm.proyectopdist.ui.home.owner.branches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.network.UploadBranchImageRequestBody
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.EditBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.locations.EditLocationResponse
import com.progdist.egm.proyectopdist.domain.*
import kotlinx.coroutines.launch

class BranchesSummaryViewModel(
    private val repository: BranchesRepository
) : ViewModel(){

    private val _getBranchResponse: MutableLiveData<Resource<GetBranchResponse>> = MutableLiveData()
    val getBranchResponse: LiveData<Resource<GetBranchResponse>> get() = _getBranchResponse

    private val _editBranchResponse: MutableLiveData<Resource<EditBranchResponse>> = MutableLiveData()
    val editBranchResponse: LiveData<Resource<EditBranchResponse>> get() = _editBranchResponse

    private val _editBranchLocationResponse: MutableLiveData<Resource<EditLocationResponse>> = MutableLiveData()
    val editBranchLocationResponse: LiveData<Resource<EditLocationResponse>> get() = _editBranchLocationResponse

    fun getBranch(
        where: String,
        id_branch: Int
    ) = viewModelScope.launch {
        val getBranchUseCase = GetBranch()
        _getBranchResponse.value = (getBranchUseCase(repository, where, id_branch) as Resource<GetBranchResponse>?)!!

    }

    fun editBranchLocation(
        id: Int,
        nameId: String,
        address: String,
        city: String,
        estate: String,
        country: String
    ) = viewModelScope.launch {

        val editBranchLocationUseCase = EditBranchLocation()
        _editBranchLocationResponse.value = (editBranchLocationUseCase(repository,id,nameId, address, city, estate, country) as Resource<EditLocationResponse>?)!!

    }

    fun editBranch(
        id: Int,
        nameId: String,
        name: String,
        phone: String,
        description: String
    ) = viewModelScope.launch {

        val editBranchUseCase = EditBranch()
        _editBranchResponse.value = (editBranchUseCase(repository,id, nameId, name, phone, description) as Resource<EditBranchResponse>?)!!

    }
}