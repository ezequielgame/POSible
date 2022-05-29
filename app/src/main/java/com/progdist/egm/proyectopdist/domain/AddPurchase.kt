package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository

class AddPurchase {

    suspend operator fun invoke(repository: SalesRepository,
                                idBranchPurchase: Int,
                                idCustomerPurchase: Int,
                                idUserPurchase: Int,
                                total: Float,
                                quantity: Int,
                                idPaymentPurchase: Int,
                                idEmployeePurchase: Int? = 1): Resource<Any> =
        repository.addPurchase(idBranchPurchase, idCustomerPurchase, idUserPurchase, total,quantity, idPaymentPurchase,idEmployeePurchase)
    
}