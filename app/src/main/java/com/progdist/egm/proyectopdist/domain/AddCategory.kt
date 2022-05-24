package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class AddCategory {

    suspend operator fun invoke(repository: InventoryRepository,
                                idUser: Int,
                                name: String,
                                description: String): Resource<Any> =
        repository.addCategory(idUser, name, description)

}