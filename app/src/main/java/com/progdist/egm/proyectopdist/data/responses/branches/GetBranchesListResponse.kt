package com.progdist.egm.proyectopdist.data.responses.branches

data class GetBranchesListResponse(
    val result: List<Branch>,
    val status: Int,
    val total: Int
)