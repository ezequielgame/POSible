package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class AddSupplier {

    suspend operator fun invoke(repository: InventoryRepository,
                                idUserSupplier: Int,
                                name: String,
                                phone: String,
                                mail: String): Resource<Any> =
        repository.addSupplier(idUserSupplier,name,phone, mail)

}