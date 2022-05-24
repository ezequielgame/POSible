package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class EditSupplier {

    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String,
                                name: String,
                                phone: String,
                                mail: String): Resource<Any> =
        repository.editSupplier(id, nameId, name, phone, mail)

}