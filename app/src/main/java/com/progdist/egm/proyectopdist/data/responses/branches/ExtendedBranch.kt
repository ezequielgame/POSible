package com.progdist.egm.proyectopdist.data.responses.branches

data class ExtendedBranch(
    val address_location: String,
    val city_location: String,
    val country_location: String,
    val date_created_branch: Any,
    val date_created_location: Any,
    val date_updated_branch: String,
    val date_updated_location: String,
    var description_branch: String,
    val estate_location: String,
    val id_branch: Int,
    val id_location: Int,
    val id_location_branch: Int,
    val id_user_branch: Int,
    val img_path_branch: String,
    var name_branch: String,
    var phone_number_branch: String
)