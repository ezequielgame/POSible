package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class EditBranchLocation {

    suspend operator fun invoke(repository: BranchesRepository,
                                id: Int,
                                nameId: String,
                                address: String,
                                city: String,
                                estate: String,
                                country: String): Resource<Any> =
        repository.editBranchLocation(id, nameId, address, city, estate, country)

}