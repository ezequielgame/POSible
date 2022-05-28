package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class EditItemStock {
    suspend operator fun invoke(repository: SalesRepository,
                                id: Int,
                                nameId: String,
                                stock: Int): Resource<Any> =
        repository.editItemStock(id, nameId, stock)

}