package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.home.GetUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET("users")
    suspend fun getUser(
        @Query("linkTo") where: String,
        @Query("equalTo") id_user: Int,
        @Query("rel") rel: String? = "users,roles",
        @Query("relType") relType: String? = "user,role"
    ) : GetUserResponse


    @GET("branches")
    suspend fun getBranch(
        @Query("linkTo") where: String,
        @Query("equalTo") id_branch: Int,
        @Query("rel") rel: String? = "branches,locations",
        @Query("relType") relType: String? = "branch,location"
    ) : GetBranchResponse

}