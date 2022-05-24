package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.branches.AddBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.DeleteBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.EditBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.*
import retrofit2.http.*

interface InventoryApi {

    // UTIL

    @GET("branches")
    suspend fun getBranchInInventory(
        @Query("linkTo") where: String,
        @Query("equalTo") id_branch: Int
    ) : GetBranchesListResponse


    //CATEGORIES

    @GET("categories")
    suspend fun getCategories(
        @Query("linkTo") where: String,
        @Query("equalTo") idBranch: Int,
        @Query("rel") rel: String? = "categories,users",
        @Query("relType") relType: String? = "category,user"
    ) : GetCategoriesResponse

    @FormUrlEncoded
    @POST("categories")
    suspend fun addCategory( //Call asynchronously
        @Field("id_user_category") id_user: Int,
        @Field("name_category") name: String,
        @Field("description_category") description: String
    ) : AddCategoryResponse

    @FormUrlEncoded
    @PUT("categories")
    suspend fun editCategory(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("name_category") name: String,
        @Field("description_category") description: String
    ) : EditCategoryResponse

    @DELETE("categories")
    suspend fun deleteCategory(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteCategoryResponse

    //SUPPLIERS

    @GET("suppliers")
    suspend fun getSuppliers(
        @Query("linkTo") where: String,
        @Query("equalTo") idUser: Int,
        @Query("rel") rel: String? = "suppliers,users",
        @Query("relType") relType: String? = "supplier,user"
    ) : GetSuppliersResponse

    @FormUrlEncoded
    @POST("suppliers")
    suspend fun addSupplier( //Call asynchronously
        @Field("id_user_supplier") idUserSupplier: Int,
        @Field("name_supplier") name: String,
        @Field("phone_number_supplier") phone: String,
        @Field("mail_supplier") mail: String
    ) : AddSupplierResponse

    @FormUrlEncoded
    @PUT("suppliers")
    suspend fun editSupplier(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("name_supplier") name: String,
        @Field("phone_number_supplier") phone: String,
        @Field("mail_supplier") mail: String
    ) : EditSupplierResponse

    @DELETE("suppliers")
    suspend fun deleteSupplier(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteSupplierResponse

}