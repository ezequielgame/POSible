package com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetRolesResponse
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.domain.AddEmployee
import com.progdist.egm.proyectopdist.domain.GetBranch
import com.progdist.egm.proyectopdist.domain.GetRoles
import kotlinx.coroutines.launch

class AddEmployeeViewModel(
    private val repository: ManageEmployeesRepository
) : ViewModel() {

    private val _getBranchResponse: MutableLiveData<Resource<GetBranchResponse>> = MutableLiveData()
    val getBranchResponse: LiveData<Resource<GetBranchResponse>> get() = _getBranchResponse

    private val _getRolesResponse: MutableLiveData<Resource<GetRolesResponse>> = MutableLiveData()
    val getRolesResponse: LiveData<Resource<GetRolesResponse>> get() = _getRolesResponse

    private val _addEmployeeResponse: MutableLiveData<Resource<AddResponse>> = MutableLiveData()
    val addEmployeeResponse: LiveData<Resource<AddResponse>> get() = _addEmployeeResponse

    fun getBranch(
        where: String,
        id_branch: Int
    ) = viewModelScope.launch {
        val getBranchUseCase = GetBranch()
        _getBranchResponse.value = (getBranchUseCase(repository, where, id_branch) as Resource<GetBranchResponse>?)!!
    }

    fun getRoles(
        where: String,
        idWhere: Int
    ) = viewModelScope.launch {
        val getRolesUseCase = GetRoles()
        _getRolesResponse.value = (getRolesUseCase(repository, where, idWhere.toString()) as Resource<GetRolesResponse>?)!!
    }

    fun addEmployee(
        idUserEmployee: Int,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        password: String,
        phone: String,
        idRole: Int
    ) = viewModelScope.launch {
        val addEmployeeUseCase = AddEmployee()
        _addEmployeeResponse.value = (addEmployeeUseCase(repository, idUserEmployee, idBranchEmployee, code, name, mail, password, phone, idRole) as Resource<AddResponse>)
    }

}