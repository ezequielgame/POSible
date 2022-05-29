package com.progdist.egm.proyectopdist.data.responses.sales

data class Purchase(
    val date_created_purchase: String,
    val id_branch_purchase: Int,
    val id_employee_purchase: Int,
    val id_payment_type_purchase: Int,
    val id_purchase: Int,
    val id_supplier_purchase: Int,
    val id_user_purchase: Int,
    val name_branch: String,
    val name_employee: String,
    val name_payment_type: String,
    val name_user: String,
    val total_purchase: Int,
    val total_quantity_purchase: Int
)