package com.progdist.egm.proyectopdist.data.responses.sales

data class GetSalesItemsResponse(
    val result: List<SaleItem>,
    val status: Int,
    val total: Int
)