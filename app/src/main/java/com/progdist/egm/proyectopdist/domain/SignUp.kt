package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.AuthRepository
import com.progdist.egm.proyectopdist.data.responses.auth.RegisterResponse

class SignUp() {

    suspend operator fun invoke(repository: AuthRepository,
                                name: String,
                                email: String,
                                password: String,
                                businessName: String,): Resource<RegisterResponse>? =
        repository.register("register", name,email, password, businessName)

}