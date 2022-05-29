package com.progdist.egm.proyectopdist.data.responses.sales

data class GetPurchasesResponse(
    val result: List<Purchase>,
    val status: Int,
    val total: Int
)