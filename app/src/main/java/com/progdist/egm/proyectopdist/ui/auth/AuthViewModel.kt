package com.progdist.egm.proyectopdist.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.data.responses.auth.EmployeeLoginResponse
import com.progdist.egm.proyectopdist.data.responses.auth.LoginResponse
import com.progdist.egm.proyectopdist.data.responses.auth.RegisterResponse
import com.progdist.egm.proyectopdist.domain.Login
import com.progdist.egm.proyectopdist.domain.SignUp
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData() //Put the value
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse //Access outside the class, inmutable

    private val _employeeLoginResponse : MutableLiveData<Resource<EmployeeLoginResponse>> = MutableLiveData() //Put the value
    val employeeLoginResponse: LiveData<Resource<EmployeeLoginResponse>> get() = _employeeLoginResponse //Access outside the class, inmutable

    private val _registerResponse : MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()
    val registerResponse: LiveData<Resource<RegisterResponse>> get() = _registerResponse

    fun login(
        domain: String,
        email: String,
        password: String
    ) = viewModelScope.launch {
        val loginUseCase = Login()
        when(domain){
            "employees"->{
                _employeeLoginResponse.value = (loginUseCase(repository, domain, email, password) as Resource<EmployeeLoginResponse>?)!!
            }
            "users" -> {
                _loginResponse.value = (loginUseCase(repository, domain, email, password) as Resource<LoginResponse>?)!!
            }
        }

    }

    fun register(
        email: String,
        password: String,
        business_name: String
    ) = viewModelScope.launch {
        val signUpUseCase = SignUp()
        _registerResponse.value = signUpUseCase(repository,email,password,business_name)!!
    }

    fun saveAuthToken(token:String) = viewModelScope.launch {
        repository.saveAuthToken(token)
    }

}