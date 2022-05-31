package com.progdist.egm.proyectopdist.data.network

import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchResponse
import com.progdist.egm.proyectopdist.data.responses.branches.GetBranchesListResponse
import com.progdist.egm.proyectopdist.data.responses.employees.GetEmployeesResponse
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

    @GET("employees")
    suspend fun getEmployees(
        @Query("linkTo") where: String,
        @Query("equalTo") idWhere: String,
        @Query("rel") rel: String? = "employees,roles,branches",
        @Query("relType") relType: String? = "employee,role,branch",
        @Query("select") select: String? = "id_employee,id_user_employee,id_branch_employee,id_role_employee,code_employee,name_employee,email_employee,phone_number_employee,name_role,name_branch",
    ) : GetEmployeesResponse

    @GET("branches")
    suspend fun getBranch(
        @Query("linkTo") where: String,
        @Query("equalTo") id_branch: Int,
        @Query("rel") rel: String? = "branches,locations",
        @Query("relType") relType: String? = "branch,location"
    ) : GetBranchResponse

}