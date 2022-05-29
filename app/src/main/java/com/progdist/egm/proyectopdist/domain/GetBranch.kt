package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class GetBranch {

    suspend operator fun invoke(repository: BranchesRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(where, id_branch)


    suspend operator fun invoke(repository: InventoryRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(where, id_branch)


    suspend operator fun invoke(repository: SalesRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(where, id_branch)

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(where, id_branch)

}