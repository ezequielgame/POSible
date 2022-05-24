package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class GetCategories {

    suspend operator fun invoke(repository: InventoryRepository,
                                where: String,
                                branchId: Int): Resource<Any> =
        repository.getCategories(where, branchId)

}