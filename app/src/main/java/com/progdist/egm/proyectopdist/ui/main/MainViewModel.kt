package com.progdist.egm.proyectopdist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.MainRepository
import com.progdist.egm.proyectopdist.data.responses.main.ValidateEmployeeTokenResponse
import com.progdist.egm.proyectopdist.data.responses.main.ValidateOwnerTokenResponse
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: MainRepository
) : ViewModel(){

    private val _validateOwnerTokenResponse : MutableLiveData<Resource<ValidateOwnerTokenResponse>> = MutableLiveData()
    val validateOwnerTokenResponse: LiveData<Resource<ValidateOwnerTokenResponse>> get() = _validateOwnerTokenResponse


    private val _validateEmployeeTokenResponse : MutableLiveData<Resource<ValidateEmployeeTokenResponse>> = MutableLiveData()
    val validateEmployeeTokenResponse: LiveData<Resource<ValidateEmployeeTokenResponse>> get() = _validateEmployeeTokenResponse

    // Use cases

    fun validateOwnerToken(

    ) = viewModelScope.launch {
        _validateOwnerTokenResponse.value = repository.validateOwnerToken()
    }

    fun validateEmployeeToken(

    ) = viewModelScope.launch {
        _validateEmployeeTokenResponse.value = repository.validateEmployeeToken()
    }


}