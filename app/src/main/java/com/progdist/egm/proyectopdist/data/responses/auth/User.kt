package com.progdist.egm.proyectopdist.data.responses.auth

data class User(
    val business_name_user: String,
    val date_created_user: Any,
    val date_updated_user: String,
    val name_user: String,
    val id_role_user: Int,
    val email_user: String,
    val id_currency_user: Any,
    val img_path_user: String,
    val id_user: Int,
    val token_exp_user: Int,
    val token_user: String
)