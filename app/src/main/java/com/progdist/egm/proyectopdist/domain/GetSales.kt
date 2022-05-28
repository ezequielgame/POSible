package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetSales {

    suspend operator fun invoke(repository: SalesRepository,
                                where: String,
                                between1: String,
                                between2: String,
                                filterIn: String,
                                filterTo: String): Resource<Any> =
        repository.getSalesRange(where, between1, between2, filterIn, filterTo)

}