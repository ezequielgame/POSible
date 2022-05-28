package com.progdist.egm.proyectopdist.data.responses.sales

data class GetSalesResponse(
    val result: List<Sale>,
    val status: Int,
    val total: Int
)