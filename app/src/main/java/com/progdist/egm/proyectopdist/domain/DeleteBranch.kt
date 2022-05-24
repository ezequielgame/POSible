package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class DeleteBranch {

    suspend operator fun invoke(repository: BranchesRepository,
                                id: Int,
                                nameId: String): Resource<Any> =
        repository.deleteBranch(id, nameId)

}