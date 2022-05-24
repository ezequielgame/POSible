package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class GetBranchesList {

    suspend operator fun invoke(repository: BranchesRepository,
                                where: String,
                                id_user: String): Resource<Any> =
        repository.getBranchesList(where, id_user)

}