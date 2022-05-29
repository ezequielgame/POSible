package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class AddPurchaseItem {

    suspend operator fun invoke(repository: SalesRepository,
                                idItem: Int,
                                idPurchase: Int,
                                quantity: Int,
                                total: Float): Resource<Any> =
        repository.addItemPurchase(idItem, idPurchase, quantity, total)
}