package com.progdist.egm.proyectopdist.data.responses.sales

data class Customer(
    val code_customer: String,
    val date_created_customer: String,
    val date_updated_customer: String,
    val id_customer: Int,
    val id_location_customer: Any,
    val id_user_customer: Int,
    val mail_customer: Any,
    val name_customer: String,
    val notes_customer: String,
    val phone_number_customer: Any
)