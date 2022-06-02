package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.branches.*
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import retrofit2.http.*

interface BranchesApi {

    @FormUrlEncoded
    @POST("locations")
    suspend fun addBranchLocation( //Call asynchronously
        @Field("address_location") address: String,
        @Field("city_location") city: String,
        @Field("estate_location") estate: String,
        @Field("country_location") country: String
    ) : AddResponse

    @FormUrlEncoded
    @POST("branches")
    suspend fun addBranch( //Call asynchronously
        @Field("id_user_branch") id_user: String,
        @Field("name_branch") name: String,
        @Field("id_location_branch") id_location: String,
        @Field("phone_number_branch") phone: String,
        @Field("description_branch") description: String
    ) : AddResponse

    @GET("branches")
    suspend fun getBranchesList(
        @Query("linkTo") where: String,
        @Query("equalTo") id_user: String
    ) : GetBranchesListResponse

    @GET("branches")
    suspend fun getBranch(
        @Query("linkTo") where: String,
        @Query("equalTo") id_branch: Int,
        @Query("rel") rel: String? = "branches,locations",
        @Query("relType") relType: String? = "branch,location"
    ) : GetBranchResponse

    @FormUrlEncoded
    @PUT("locations")
    suspend fun editBranchLocation(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("address_location") address: String,
        @Field("city_location") city: String,
        @Field("estate_location") estate: String,
        @Field("country_location") country: String
    ) : EditResponse

    @FormUrlEncoded
    @PUT("branches")
    suspend fun editBranch(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("name_branch") name: String,
        @Field("phone_number_branch") phone: String,
        @Field("description_branch") description: String
    ) : EditResponse

    @DELETE("branches")
    suspend fun deleteBranch(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse

    @DELETE("locations")
    suspend fun deleteBranchLocation(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse

}