package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository

class DeleteEmployee {

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                id: Int,
                                nameId: String): Resource<Any> =
        repository.deleteEmployee(id, nameId)

}