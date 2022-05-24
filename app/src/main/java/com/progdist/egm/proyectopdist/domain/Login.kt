package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.data.responses.auth.LoginResponse

class Login {

    suspend operator fun invoke(repository: AuthRepository,domain:String,email: String, password: String): Resource<Any> =
        repository.login(domain,"login", email, password)

}