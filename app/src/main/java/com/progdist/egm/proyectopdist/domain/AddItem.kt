package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository

class AddItem {

    suspend operator fun invoke(repository: InventoryRepository,
                                idBranchItem: Int,
                                code: String,
                                name: String,
                                sale: Float,
                                purchase: Float,
                                stock: Int,
                                idCategory: Int,
                                idSupplier: Int): Resource<Any> =
        repository.addItem(idBranchItem, code, name, sale, purchase,stock, idCategory, idSupplier)
}