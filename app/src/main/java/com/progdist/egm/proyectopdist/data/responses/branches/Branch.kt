package com.progdist.egm.proyectopdist.data.responses.branches

data class Branch(
    val date_created_branch: Any,
    val date_updated_branch: String,
    var description_branch: String,
    val id_branch: Int,
    val id_location_branch: Int,
    val id_user_branch: Int,
    var name_branch: String,
    val img_path_branch: String,
    var phone_number_branch: String
)