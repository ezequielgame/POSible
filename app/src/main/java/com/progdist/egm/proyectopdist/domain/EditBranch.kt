package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class EditBranch {

    suspend operator fun invoke(repository: BranchesRepository,
                                id: Int,
                                nameId: String,
                                name: String,
                                phone: String,
                                description: String): Resource<Any> =
        repository.editBranch(id, nameId, name, phone, description)

}