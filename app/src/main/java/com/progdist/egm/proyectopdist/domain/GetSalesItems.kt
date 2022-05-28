package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetSalesItems {

    suspend operator fun invoke(repository: SalesRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getSalesItems(where, idWhere)

}