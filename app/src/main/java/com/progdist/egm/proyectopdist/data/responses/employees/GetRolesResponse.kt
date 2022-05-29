package com.progdist.egm.proyectopdist.data.responses.employees

data class GetRolesResponse(
    val result: List<Role>,
    val status: Int,
    val total: Int
)