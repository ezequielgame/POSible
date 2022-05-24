package com.progdist.egm.proyectopdist.data.responses.inventory

data class GetSuppliersResponse(
    val result: List<Supplier>,
    val status: Int,
    val total: Int
)