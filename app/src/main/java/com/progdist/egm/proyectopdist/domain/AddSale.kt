package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class AddSale {

    suspend operator fun invoke(repository: SalesRepository,
                                idBranchSale: Int,
                                idCustomerSale: Int,
                                idUserSale: Int,
                                total: Float,
                                quantity: Int,
                                idPaymentSale: Int,
                                idEmployeeSale: Int? = 1): Resource<Any> =
        repository.addSale(idBranchSale, idCustomerSale, idUserSale, total,quantity, idPaymentSale,idEmployeeSale)

}