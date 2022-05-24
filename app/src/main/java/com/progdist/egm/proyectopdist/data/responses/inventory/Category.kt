package com.progdist.egm.proyectopdist.data.responses.inventory

data class Category(
    val business_name_user: String,
    val date_created_category: Any,
    val date_created_user: Any,
    val date_updated_category: String,
    val date_updated_user: String,
    val description_category: String,
    val email_user: String,
    val id_category: Int,
    val id_currency_user: Any,
    val id_role_user: Int,
    val id_user: Int,
    val id_user_category: Int,
    val img_path_user: String,
    val name_category: String,
    val name_user: String,
    val password_user: String,
    val token_exp_user: String,
    val token_user: String
)