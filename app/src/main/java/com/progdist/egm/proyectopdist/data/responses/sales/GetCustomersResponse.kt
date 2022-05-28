package com.progdist.egm.proyectopdist.data.responses.sales

data class GetCustomersResponse(
    val result: List<Customer>,
    val status: Int,
    val total: Int
)