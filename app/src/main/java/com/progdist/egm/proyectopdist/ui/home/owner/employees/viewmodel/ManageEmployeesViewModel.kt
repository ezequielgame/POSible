package com.progdist.egm.proyectopdist.ui.home.owner.employees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.domain.DeleteCategory
import com.progdist.egm.proyectopdist.domain.DeleteEmployee
import com.progdist.egm.proyectopdist.domain.GetEmployees
import kotlinx.coroutines.launch

class ManageEmployeesViewModel(
    private val repository: ManageEmployeesRepository
) : ViewModel() {

    private val _getEmployeesResponse: MutableLiveData<Resource<GetEmployeesResponse>> = MutableLiveData()
    val getEmployeesResponse: LiveData<Resource<GetEmployeesResponse>> get() = _getEmployeesResponse

    private val _deleteEmployeeResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    val deleteEmployeeResponse: LiveData<Resource<DeleteResponse>> get() = _deleteEmployeeResponse

    fun getEmployees(
        where: String,
        idWhere: String
    ) = viewModelScope.launch{
        val getEmployeesUseCase = GetEmployees()
        _getEmployeesResponse.value = (getEmployeesUseCase(repository,where,idWhere) as Resource<GetEmployeesResponse>)
    }

    fun deleteEmployee(
        id: Int,
        nameId: String
    ) = viewModelScope.launch {
        val deleteEmployeeUseCase = DeleteEmployee()
        _deleteEmployeeResponse.value = (deleteEmployeeUseCase(repository,id, nameId) as Resource<DeleteResponse>)
    }

}