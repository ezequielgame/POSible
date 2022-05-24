package com.progdist.egm.proyectopdist.data.responses.branches

data class GetBranchResponse(
    val result: List<ExtendedBranch>,
    val status: Int,
    val total: Int
)