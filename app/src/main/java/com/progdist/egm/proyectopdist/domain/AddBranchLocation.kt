package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class AddBranchLocation {

    suspend operator fun invoke(repository: BranchesRepository,
                                address: String,
                                city: String,
                                estate: String,
                                country: String): Resource<Any> =
        repository.addBranchLocation(address,city,estate,country)

}