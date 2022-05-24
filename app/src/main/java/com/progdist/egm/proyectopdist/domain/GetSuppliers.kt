package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class GetSuppliers {

    suspend operator fun invoke(repository: InventoryRepository,
                                where: String,
                                userId: Int): Resource<Any> =
        repository.getSuppliers(where, userId)

}