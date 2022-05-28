package com.progdist.egm.proyectopdist.data.responses.sales

data class Sale(
    val date_created_sale: String,
    val id_branch_sale: Int,
    val id_customer_sale: Int,
    val id_employee_sale: Int,
    val id_payment_type_sale: Int,
    val id_sale: Int,
    val id_user_sale: Int,
    val name_branch: String,
    val name_employee: String,
    val name_payment_type: String,
    val name_user: String,
    val total_quantity_sale: Int,
    val total_sale: Double
)