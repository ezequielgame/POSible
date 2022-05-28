package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetPaymentTypes {

    suspend operator fun invoke(repository: SalesRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getPaymentTypes(where, idWhere)

}