package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class DeleteCategory {
    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String): Resource<Any> =
        repository.deleteCategory(id, nameId)

}