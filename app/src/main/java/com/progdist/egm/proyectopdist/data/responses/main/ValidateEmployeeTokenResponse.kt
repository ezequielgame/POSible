package com.progdist.egm.proyectopdist.data.responses.main

data class ValidateEmployeeTokenResponse(
    val employee: Int,
    val status: Int,
    val total: Int
)