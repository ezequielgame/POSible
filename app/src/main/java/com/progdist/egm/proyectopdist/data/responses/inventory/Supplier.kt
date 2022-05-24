package com.progdist.egm.proyectopdist.data.responses.inventory

data class Supplier(
    val business_name_user: String,
    val date_created_supplier: Any,
    val date_created_user: Any,
    val date_updated_supplier: String,
    val date_updated_user: String,
    val email_user: String,
    val id_currency_user: Any,
    val id_role_user: Int,
    val id_supplier: Int,
    val id_user: Int,
    val id_user_supplier: Int,
    val img_path_user: String,
    val mail_supplier: String,
    val name_supplier: String,
    val name_user: String,
    val password_user: String,
    val phone_number_supplier: String,
    val token_exp_user: String,
    val token_user: String
)