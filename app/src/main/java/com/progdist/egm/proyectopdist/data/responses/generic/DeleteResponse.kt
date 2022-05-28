package com.progdist.egm.proyectopdist.data.responses.generic

data class DeleteResponse(
    val result: DeleteResult,
    val status: Int,
    val total: Int
)