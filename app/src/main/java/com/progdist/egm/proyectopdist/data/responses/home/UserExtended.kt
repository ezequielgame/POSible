package com.progdist.egm.proyectopdist.data.responses.home

data class UserExtended(
    val business_name_user: String,
    val date_created_role: Any,
    val date_created_user: Any,
    val date_updated_role: String,
    val date_updated_user: String,
    val description_role: String,
    val email_user: String,
    val name_user: String,
    val id_currency_user: Any,
    val id_role: Int,
    val id_role_user: Int,
    val id_user: Int,
    val id_user_role: Int,
    val name_role: String,
    val img_path_user: String,
    val password_user: String,
    val token_exp_user: String,
    val token_user: String
)