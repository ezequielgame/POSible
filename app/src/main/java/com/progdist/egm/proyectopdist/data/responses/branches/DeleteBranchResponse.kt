package com.progdist.egm.proyectopdist.data.responses.branches

data class DeleteBranchResponse(
    val result: DeleteBranchResult,
    val status: Int,
    val total: Int
)