package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetItems {

    suspend operator fun invoke(repository: InventoryRepository,
                                where: String,
                                idWhere: Int): Resource<Any> =
        repository.getItems(where, idWhere)

    suspend operator fun invoke(repository: SalesRepository,
                                where: String,
                                idWhere: String): Resource<Any> =
        repository.getItems(where, idWhere)


}