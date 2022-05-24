package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class EditCategory {

    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String,
                                name: String,
                                description: String): Resource<Any> =
        repository.editCategory(id, nameId, name, description)

}