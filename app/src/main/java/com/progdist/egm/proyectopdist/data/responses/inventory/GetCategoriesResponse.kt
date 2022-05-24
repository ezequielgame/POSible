package com.progdist.egm.proyectopdist.data.responses.inventory

data class GetCategoriesResponse(
    val result: List<Category>,
    val status: Int,
    val total: Int
)