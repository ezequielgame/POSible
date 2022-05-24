package com.progdist.egm.proyectopdist.data.responses.inventory

data class AddSupplierResponse(
    val result: AddSupplierResult,
    val status: Int,
    val total: Int
)