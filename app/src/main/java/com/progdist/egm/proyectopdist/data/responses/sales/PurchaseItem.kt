package com.progdist.egm.proyectopdist.data.responses.sales

data class PurchaseItem(
    val code_item: String,
    val date_created_purchase_item: String,
    val date_updated_purchase_item: String,
    val id_branch_purchase: Int,
    val id_category_item: Int,
    val id_employee_purchase: Int,
    val id_item: Int,
    val id_payment_type_purchase: Int,
    val id_purchase: Int,
    val id_purchase_item: Int,
    val id_supplier_item: Int,
    val id_supplier_purchase: Int,
    val id_user_purchase: Int,
    val name_item: String,
    val purchase_price_item: Double,
    val quantity_purchase_item: Int,
    val stock_item: Int,
    val total_purchase: Double,
    val total_purchase_item: Double,
    val total_quantity_purchase: Int
)