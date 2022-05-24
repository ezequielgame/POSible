package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository

class GetBranch {

    suspend operator fun invoke(repository: BranchesRepository,
                                where: String,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(where, id_branch)

}