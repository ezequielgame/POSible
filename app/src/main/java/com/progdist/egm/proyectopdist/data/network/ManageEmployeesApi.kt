package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.auth.RegisterResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetRolesResponse
import com.progdist.egm.proyectopdist.data.responses.generic.AddResponse
import com.progdist.egm.proyectopdist.data.responses.generic.DeleteResponse
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import retrofit2.http.*

interface ManageEmployeesApi {

    @GET("employees")
    suspend fun getEmployees(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
        @Query("rel") rel: String? = "employees,roles,branches",
        @Query("relType") relType: String? = "employee,role,branch",
        @Query("select") select: String? = "id_employee,id_user_employee,id_branch_employee,id_role_employee,code_employee,name_employee,email_employee,phone_number_employee,name_role,name_branch"
    ) : GetEmployeesResponse

    @FormUrlEncoded
    @POST("employees")
    suspend fun addEmployee(
        @Field("id_user_employee") idUserEmployee: Int,
        @Field("id_branch_employee") idBranchEmployee: Int,
        @Field("code_employee") code: String,
        @Field("name_employee") name: String,
        @Field("email_employee") email: String,
        @Field("password_employee") password: String,
        @Field("phone_number_employee") phone: String,
        @Field("id_role_employee") idRole: Int,
        @Query("action") action: String? = "register",
        @Field("suffix") suffix: String? = "employee"
    ) : AddResponse

    @FormUrlEncoded
    @PUT("employees")
    suspend fun editEmployee(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("id_branch_employee") idBranchEmployee: Int,
        @Field("code_employee") code: String,
        @Field("name_employee") name: String,
        @Field("email_employee") email: String,
        @Field("phone_number_employee") phone: String,
        @Field("id_role_employee") idRole: Int
    ) : EditResponse

    @FormUrlEncoded
    @PUT("employees")
    suspend fun editEmployeePass(
        @Query("id") id: Int,
        @Query("nameId") nameId: String,
        @Field("id_branch_employee") idBranchEmployee: Int,
        @Field("code_employee") code: String,
        @Field("name_employee") name: String,
        @Field("email_employee") email: String,
        @Field("password_employee") password: String,
        @Field("phone_number_employee") phone: String,
        @Field("id_role_employee") idRole: Int
    ) : EditResponse

    @DELETE("employees")
    suspend fun deleteEmployee(
        @Query("id") id: Int,
        @Query("nameId") nameId: String
    ) : DeleteResponse

    @GET("branches")
    suspend fun getBranch(
        @Query("linkTo") where: String,
        @Query("equalTo") id_branch: Int,
        @Query("rel") rel: String? = "branches,locations",
        @Query("relType") relType: String? = "branch,location"
    ) : GetBranchResponse

    @GET("roles")
    suspend fun getRoles(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
        @Query("select") select: String? = "id_role,name_role,description_role,id_user_role"
    ) : GetRolesResponse

}