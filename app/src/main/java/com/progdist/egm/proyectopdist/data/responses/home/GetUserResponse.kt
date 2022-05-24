package com.progdist.egm.proyectopdist.data.responses.home

data class GetUserResponse(
    val result: List<UserExtended>,
    val status: Int,
    val total: Int
)