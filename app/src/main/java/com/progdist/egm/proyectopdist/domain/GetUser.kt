package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.HomeRepository

class GetUser {

    suspend operator fun invoke(repository: HomeRepository, id_user: Int): Resource<Any> =
        repository.getUser(id_user)

}