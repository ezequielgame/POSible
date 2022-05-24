package com.progdist.egm.proyectopdist.data.responses.inventory

data class GetItemsResponse(
    val result: List<Item>,
    val status: Int,
    val total: Int
)