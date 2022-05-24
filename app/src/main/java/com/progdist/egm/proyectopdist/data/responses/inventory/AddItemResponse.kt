package com.progdist.egm.proyectopdist.data.responses.inventory

data class AddItemResponse(
    val result: AddItemResult,
    val status: Int,
    val total: Int
)