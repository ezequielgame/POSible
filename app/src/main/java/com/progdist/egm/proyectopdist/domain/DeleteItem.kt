package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository

class DeleteItem {

    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String): Resource<Any> =
        repository.deleteItem(id, nameId)

}