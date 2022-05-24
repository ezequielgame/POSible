package com.progdist.egm.proyectopdist.data.responses.inventory

data class AddCategoryResponse(
    val result: AddCategoryResult,
    val status: Int,
    val total: Int
)