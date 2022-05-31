package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class EditCode {

    suspend operator fun invoke(repository: SalesRepository,
                                id: Int,
                                nameId: String,
                                code: String): Resource<Any> =
        repository.editCode(id, nameId, code)

}