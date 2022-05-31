package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.HomeRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository

class GetEmployees {

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getEmployees(where, idWhere)

    suspend operator fun invoke(repository: HomeRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getEmployees(where, idWhere)

}