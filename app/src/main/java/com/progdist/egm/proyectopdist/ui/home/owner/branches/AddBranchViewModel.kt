package com.progdist.egm.proyectopdist.ui.home.owner.branches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.responses.auth.EmployeeLoginResponse
import com.progdist.egm.proyectopdist.data.responses.auth.LoginResponse
import com.progdist.egm.proyectopdist.data.responses.branches.AddBranchResponse
import com.progdist.egm.proyectopdist.data.responses.locations.AddLocationResponse
import com.progdist.egm.proyectopdist.domain.AddBranch
import com.progdist.egm.proyectopdist.domain.AddBranchLocation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddBranchViewModel(
    private val repository: BranchesRepository
) : ViewModel(){

    private val _addBranchLocationResponse : MutableLiveData<Resource<AddLocationResponse>> = MutableLiveData() //Put the value
    val addBranchLocationResponse: LiveData<Resource<AddLocationResponse>> get() = _addBranchLocationResponse

    private val _addBranchResponse : MutableLiveData<Resource<AddBranchResponse>> = MutableLiveData() //Put the value
    val addBranchResponse: LiveData<Resource<AddBranchResponse>> get() = _addBranchResponse

    fun addBranchLocation(
        address: String,
        city: String,
        estate: String,
        country: String
    ) = viewModelScope.launch {
        val addBranchLocationUseCase = AddBranchLocation()
        _addBranchLocationResponse.value = (addBranchLocationUseCase(repository, address,city,estate, country) as Resource<AddLocationResponse>?)!!
    }

    fun addBranch(
        id_user: String,
        name: String,
        id_location: String,
        phone: String,
        description: String,
    ) = viewModelScope.launch {
        val addBranchUseCase = AddBranch()
        _addBranchResponse.value = (addBranchUseCase(repository, id_user, name, id_location, phone, description)  as Resource<AddBranchResponse>?)!!
    }



}