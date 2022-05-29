package com.progdist.egm.proyectopdist.data.responses.sales

data class GetPurchasesItemsResponse(
    val result: List<PurchaseItem>,
    val status: Int,
    val total: Int
)