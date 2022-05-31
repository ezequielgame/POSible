package com.progdist.egm.proyectopdist.ui.home.owner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.HomeRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.home.GetUserResponse
import com.progdist.egm.proyectopdist.domain.GetEmployees
import com.progdist.egm.proyectopdist.domain.GetHomeBranch
import com.progdist.egm.proyectopdist.domain.GetUser
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _getUserResponse : MutableLiveData<Resource<GetUserResponse>> = MutableLiveData() //Put the value
    val getUserResponse: LiveData<Resource<GetUserResponse>> get() = _getUserResponse

    private val _getBranchResponse : MutableLiveData<Resource<GetBranchResponse>> = MutableLiveData() //Put the value
    val getBranchResponse: LiveData<Resource<GetBranchResponse>> get() = _getBranchResponse

    private val _getEmployeesResponse: MutableLiveData<Resource<GetEmployeesResponse>> = MutableLiveData()
    val getEmployeesResponse: LiveData<Resource<GetEmployeesResponse>> get() = _getEmployeesResponse

    private val _saveSelectedBranchResponse: MutableLiveData<Unit> = MutableLiveData()
    val saveSelectedBranchResponse: LiveData<Unit> get() = _saveSelectedBranchResponse

    fun getUser(
        id_user: Int
    ) = viewModelScope.launch {

        val getUserUseCase = GetUser()
        _getUserResponse.value = (getUserUseCase(repository,id_user) as Resource<GetUserResponse>?)!!

    }

    fun getEmployees(
        where: String,
        idWhere: String
    ) = viewModelScope.launch {
        val getEmployeesUseCase = GetEmployees()
        _getEmployeesResponse.value = (getEmployeesUseCase(repository, where, idWhere) as Resource<GetEmployeesResponse>)
    }

    fun getBranch(
        branchId: Int
    ) = viewModelScope.launch {
        val getHomeBranchUseCase = GetHomeBranch()
        _getBranchResponse.value = (getHomeBranchUseCase(repository,branchId) as Resource<GetBranchResponse>)
    }

    fun saveIdSelectedBranch(
        branchId: Int
    ) = viewModelScope.launch {
        _saveSelectedBranchResponse.value = repository.saveIdSelectedBranch(branchId)
    }

    fun deleteAuthToken() = viewModelScope.launch {
        repository.deleteAuthToken()
    }

}