package com.progdist.egm.proyectopdist.data.responses.inventory

data class Item(
    val code_item: String,
    val date_created_branch: String,
    val date_created_category: String,
    val date_created_item: String,
    val date_created_supplier: String,
    val date_updated_branch: String,
    val date_updated_category: String,
    val date_updated_item: String,
    val date_updated_supplier: String,
    val description_branch: String,
    val description_category: String,
    val id_branch: Int,
    val id_branch_item: Int,
    val id_category: Int,
    val id_category_item: Int,
    val id_item: Int,
    val id_location_branch: Int,
    val id_supplier: Int,
    val id_supplier_item: Int,
    val id_user_branch: Int,
    val id_user_category: Int,
    val id_user_supplier: Int,
    val img_path_branch: Any,
    val img_path_item: Any,
    val mail_supplier: String,
    val name_branch: String,
    val name_category: String,
    val name_item: String,
    val name_supplier: String,
    val phone_number_branch: String,
    val phone_number_supplier: String,
    val purchase_price_item: Float,
    val sale_price_item: Float,
    val stock_item: Int
)