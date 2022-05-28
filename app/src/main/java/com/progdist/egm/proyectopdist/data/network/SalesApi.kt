package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.data.responses.inventory.GetItemsResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetCustomersResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetPaymentTypesResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetSalesItemsResponse
import com.progdist.egm.proyectopdist.data.responses.sales.GetSalesResponse
import retrofit2.http.*

interface SalesApi : InventoryApi {

    //ITEMS

    @GET("items")
    suspend fun getItems(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
        @Query("rel") rel: String? = "items,branches,categories,suppliers",
        @Query("relType") relType: String? = "item,branch,category,supplier"
    ) : GetItemsResponse

    //Payments
    @GET("payment_types")
    suspend fun getPaymentTypes(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
    ) : GetPaymentTypesResponse

    //Payments
    @GET("customers")
    suspend fun getCustomers(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
    ) : GetCustomersResponse

    //Sales
    @FormUrlEncoded
    @POST("sales")
    suspend fun addOwnerSale( //Call asynchronously
        @Field("id_branch_sale") idBranchSale: Int,
        @Field("id_customer_sale") idCustomerSale: Int,
        @Field("id_user_sale") idUserSale: Int,
        @Field("total_sale") total: Float,
        @Field("total_quantity_sale") quantity: Int,
        @Field("id_payment_type_sale") idPaymentSale: Int,
    ) : AddResponse

    @FormUrlEncoded
    @POST("sales_item")
    suspend fun addItemSale( //Call asynchronously
        @Field("id_item_sale_item") idItem: Int,
        @Field("id_sale_sale_item") idSale: Int,
        @Field("quantity_sale_item") quantity: Int,
        @Field("total_sale_item") total: Float
    ) : AddResponse

    @GET("sales")
    suspend fun getSalesRange(
        @Query("linkTo") where: String,
        @Query("between1") between1: String,
        @Query("between2") between2: String,
        @Query("filterIn") filterIn: String,
        @Query("filterTo") filterTo: String,
        @Query("select") select:String? = "id_sale,id_branch_sale,id_customer_sale,id_user_sale,id_employee_sale,total_sale,total_quantity_sale,id_payment_type_sale,name_user,name_employee,name_branch,name_payment_type,date_created_sale",
        @Query("orderBy") orderBy: String? = "id_sale",
        @Query("rel") rel: String? = "sales,users,employees,branches,payment_types",
        @Query("relType") relType: String? = "sale,user,employee,branch,payment_type",
        @Query("id_employee_sale") idEmployeeSale: Int? = 1
    ) : GetSalesResponse

    @GET("sales_item")
    suspend fun getSalesItems(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
        @Query("rel") rel: String? = "sales_item,sales,items",
        @Query("relType") relType: String? = "sale_item,sale,item",
        @Query("select") select: String? = "id_sale_item,quantity_sale_item,total_sale_item,date_created_sale_item,date_updated_sale_item,id_sale,id_branch_sale,id_customer_sale,id_user_sale,id_employee_sale,total_sale,total_quantity_sale,id_payment_type_sale,code_item,name_item,sale_price_item,stock_item,purchase_price_item,stock_item,id_category_item,id_supplier_item,id_item"
    ) : GetSalesItemsResponse

    @FormUrlEncoded
    @PUT("items")
    suspend fun editItemStock(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("stock_item") stock: Int
    ) : EditResponse

}