package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class AddBranch {

    suspend operator fun invoke(repository: BranchesRepository,
                                id_user: String,
                                name: String,
                                id_location: String,
                                phone: String,
                                description: String,): Resource<Any> =
        repository.addBranch(id_user,name, id_location, phone, description)

}