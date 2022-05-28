package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class AddSaleItem {

    suspend operator fun invoke(repository: SalesRepository,
                                idItem: Int,
                                idSale: Int,
                                quantity: Int,
                                total: Float): Resource<Any> =
        repository.addItemSale(idItem, idSale, quantity, total)

}