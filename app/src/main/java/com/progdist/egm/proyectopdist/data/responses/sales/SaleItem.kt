package com.progdist.egm.proyectopdist.data.responses.sales

data class SaleItem(
    val code_item: String,
    val date_created_sale_item: String,
    val date_updated_sale_item: String,
    val id_branch_sale: Int,
    val id_category_item: Int,
    val id_customer_sale: Int,
    val id_employee_sale: Int,
    val id_item: Int,
    val id_payment_type_sale: Int,
    val id_sale: Int,
    val id_sale_item: Int,
    val id_supplier_item: Int,
    val id_user_sale: Int,
    val name_item: String,
    val purchase_price_item: Double,
    val quantity_sale_item: Int,
    val sale_price_item: Double,
    val stock_item: Int,
    val total_quantity_sale: Int,
    val total_sale: Double,
    val total_sale_item: Double
)