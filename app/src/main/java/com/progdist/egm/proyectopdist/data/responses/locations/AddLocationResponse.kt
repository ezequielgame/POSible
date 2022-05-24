package com.progdist.egm.proyectopdist.data.responses.locations

data class AddLocationResponse(
    val result: AddLocationResult,
    val status: Int,
    val total: Int
)