package com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetRolesResponse
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.domain.EditEmployee
import com.progdist.egm.proyectopdist.domain.GetBranch
import com.progdist.egm.proyectopdist.domain.GetEmployees
import com.progdist.egm.proyectopdist.domain.GetRoles
import kotlinx.coroutines.launch

class EmployeeSummaryViewModel(
    private val repository: ManageEmployeesRepository
): ViewModel() {

    private val _getEmployeesResponse: MutableLiveData<Resource<GetEmployeesResponse>> = MutableLiveData()
    val getEmployeesResponse: LiveData<Resource<GetEmployeesResponse>> get() = _getEmployeesResponse

    private val _editEmployeeResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editEmployeeResponse: MutableLiveData<Resource<EditResponse>> get() = _editEmployeeResponse

    fun getEmployees(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getEmployeesUseCase = GetEmployees()
        _getEmployeesResponse.value = (getEmployeesUseCase(repository,where,idWhere) as Resource<GetEmployeesResponse>)
    }

    fun editEmployee(
        id: Int,
        nameId: String,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        phone: String,
        idRole: Int
    ) = viewModelScope.launch {
        val editEmployeeUseCase = EditEmployee()
        _editEmployeeResponse.value = (editEmployeeUseCase(repository, id, nameId, idBranchEmployee, code, name, mail, phone, idRole) as Resource<EditResponse>)
    }

    fun editEmployee(
        id: Int,
        nameId: String,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        password: String,
        phone: String,
        idRole: Int
    ) = viewModelScope.launch {
        val editEmployeeUseCase = EditEmployee()
        _editEmployeeResponse.value = (editEmployeeUseCase(repository, id, nameId, idBranchEmployee, code, name, mail,password, phone, idRole) as Resource<EditResponse>)
    }



    private val _getBranchResponse: MutableLiveData<Resource<GetBranchResponse>> = MutableLiveData()

    val getBranchResponse: LiveData<Resource<GetBranchResponse>> get() = _getBranchResponse
    private val _getRolesResponse: MutableLiveData<Resource<GetRolesResponse>> = MutableLiveData()

    val getRolesResponse: LiveData<Resource<GetRolesResponse>> get() = _getRolesResponse

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



}