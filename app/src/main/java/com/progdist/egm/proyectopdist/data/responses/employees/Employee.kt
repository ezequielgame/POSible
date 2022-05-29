package com.progdist.egm.proyectopdist.data.responses.employees

data class Employee(
    val code_employee: String,
    val id_branch_employee: Int,
    val id_employee: Int,
    val id_role_employee: Int,
    val id_user_employee: Int,
    val email_employee: String,
    val name_branch: String,
    val name_employee: String,
    val name_role: String,
    val phone_number_employee: String
)