package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.BranchesRepository
import com.progdist.egm.proyectopdist.data.repository.HomeRepository

class GetHomeBranch {

    suspend operator fun invoke(repository: HomeRepository,
                                id_branch: Int): Resource<Any> =
        repository.getBranch(id_branch)

}