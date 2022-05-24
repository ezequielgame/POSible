package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class DeleteSupplier {


    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String): Resource<Any> =
        repository.deleteSupplier(id, nameId)

}