package com.progdist.egm.proyectopdist.data.responses.auth

data class RegisterResponse(
    val result: RegisterResult,
    val status: Int,
    val total: Int
)