package com.progdist.egm.proyectopdist.data.responses.employees

data class GetEmployeesResponse(
    val result: List<Employee>,
    val status: Int,
    val total: Int
)