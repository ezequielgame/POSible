package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class EditItem {

    suspend operator fun invoke(repository: InventoryRepository,
                                id: Int,
                                nameId: String,
                                code: String,
                                name: String,
                                sale: Float,
                                purchase: Float,
                                stock: Int,
                                idCategory: Int,
                                idSupplier: Int): Resource<Any> =
        repository.editItem(id, nameId,code, name, sale, purchase, stock, idCategory, idSupplier)

}