package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class GetBranchInInventory {

    suspend operator fun invoke(repository: InventoryRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranchInInventory(where, id_branch)

}