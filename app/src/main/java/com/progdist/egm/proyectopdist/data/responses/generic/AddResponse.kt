package com.progdist.egm.proyectopdist.data.responses.generic

data class AddResponse(
    val result: AddResult,
    val status: Int,
    val total: Int
)