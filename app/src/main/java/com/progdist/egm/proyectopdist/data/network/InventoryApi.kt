package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.*
import retrofit2.http.*

interface InventoryApi {

    // UTIL

    @GET("branches")
    suspend fun getBranch(
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
    ) : AddResponse

    @FormUrlEncoded
    @PUT("categories")
    suspend fun editCategory(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("name_category") name: String,
        @Field("description_category") description: String
    ) : EditResponse

    @DELETE("categories")
    suspend fun deleteCategory(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse

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
    ) : AddResponse

    @FormUrlEncoded
    @PUT("suppliers")
    suspend fun editSupplier(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("name_supplier") name: String,
        @Field("phone_number_supplier") phone: String,
        @Field("mail_supplier") mail: String
    ) : EditResponse

    @DELETE("suppliers")
    suspend fun deleteSupplier(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse


    //ITEMS

    @GET("items")
    suspend fun getItems(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: Int,
        @Query("rel") rel: String? = "items,branches,categories,suppliers",
        @Query("relType") relType: String? = "item,branch,category,supplier"
    ) : GetItemsResponse

    @FormUrlEncoded
    @POST("items")
    suspend fun addItem( //Call asynchronously
        @Field("id_branch_item") idBranchItem: Int,
        @Field("code_item") code: String,
        @Field("name_item") name: String,
        @Field("sale_price_item") sale: Float,
        @Field("purchase_price_item") purchase: Float,
        @Field("stock_item") stock: Int,
        @Field("id_category_item") idCategory: Int,
        @Field("id_supplier_item") idSupplier: Int
    ) : AddResponse

    @FormUrlEncoded
    @PUT("items")
    suspend fun editItem(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("code_item") code: String,
        @Field("name_item") name: String,
        @Field("sale_price_item") sale: Float,
        @Field("purchase_price_item") purchase: Float,
        @Field("stock_item") stock: Int,
        @Field("id_category_item") idCategory: Int,
        @Field("id_supplier_item") idSupplier: Int

    ) : EditResponse

    @DELETE("items")
    suspend fun deleteItem(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse

}