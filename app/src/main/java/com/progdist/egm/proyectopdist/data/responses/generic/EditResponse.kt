package com.progdist.egm.proyectopdist.data.responses.generic

data class EditResponse(
    val result: EditResult,
    val status: Int,
    val total: Int
)