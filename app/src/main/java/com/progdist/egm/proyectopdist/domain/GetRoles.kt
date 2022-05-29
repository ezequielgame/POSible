package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetRoles {

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getRoles(where, idWhere)

}