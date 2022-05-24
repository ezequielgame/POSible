package com.progdist.egm.proyectopdist.data.responses.inventory

data class DeleteCategoryResponse(
    val result: DeleteCategoryResult,
    val status: Int,
    val total: Int
)