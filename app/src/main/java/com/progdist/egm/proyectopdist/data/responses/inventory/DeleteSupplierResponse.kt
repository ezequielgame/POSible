package com.progdist.egm.proyectopdist.data.responses.inventory

data class DeleteSupplierResponse(
    val result: DeleteSupplierResult,
    val status: Int,
    val total: Int
)