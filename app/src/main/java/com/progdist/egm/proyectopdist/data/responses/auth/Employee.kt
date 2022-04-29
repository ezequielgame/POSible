package com.progdist.egm.proyectopdist.data.responses.auth

data class Employee(
    val code_employee: String,
    val date_created_employee: Any,
    val date_updated_employee: String,
    val email_employee: String,
    val first_name_employee: String,
    val id_employee: Int,
    val id_role_employee: Any,
    val last_name_employee: String,
    val middle_name_employee: String,
    val phone_number_employee: String,
    val token_employee: String,
    val token_exp_employee: Int
)