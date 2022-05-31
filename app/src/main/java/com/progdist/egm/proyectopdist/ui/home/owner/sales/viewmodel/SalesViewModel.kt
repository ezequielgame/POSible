package com.progdist.egm.proyectopdist.ui.home.owner.sales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
import com.progdist.egm.proyectopdist.domain.GetBranch
import kotlinx.coroutines.launch

class SalesViewModel(
    private val repository: SalesRepository
) : ViewModel() {

    private val _getBranchesListResponse: MutableLiveData<Resource<GetBranchesListResponse>> = MutableLiveData()
    val getBranchesListResponse: LiveData<Resource<GetBranchesListResponse>> get() = _getBranchesListResponse

    private val _getEmployeesRespone: MutableLiveData<Resource<GetEmployeesResponse>> = MutableLiveData()
    val getEmployeesResponse: LiveData<Resource<GetEmployeesResponse>> get() = _getEmployeesRespone

    fun getBranch(
        where:String,
        id_branch: Int
    ) = viewModelScope.launch {
        val getBranchUseCase = GetBranch()
        _getBranchesListResponse.value = (getBranchUseCase(repository,where,id_branch) as Resource<GetBranchesListResponse>)
    }

}