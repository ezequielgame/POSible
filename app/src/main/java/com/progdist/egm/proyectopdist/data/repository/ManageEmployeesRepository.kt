package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.UserPreferences
import com.progdist.egm.proyectopdist.data.network.ManageEmployeesApi
import retrofit2.http.Field
import retrofit2.http.Query

class ManageEmployeesRepository(
    private val api: ManageEmployeesApi,
    private val preferences: UserPreferences
) : BaseRepository() {

    suspend fun getEmployees(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getEmployees(where, idWhere)
    }

    suspend fun addEmployee(
        idUserEmployee: Int,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        password: String,
        phone: String,
        idRole: Int
    ) = safeApiCall {
        api.addEmployee(idUserEmployee, idBranchEmployee, code, name, mail, password, phone, idRole)
    }

    suspend fun editEmployee(
        id: Int,
        nameId: String,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        phone: String,
        idRole: Int
    ) = safeApiCall {
        api.editEmployee(id, nameId, idBranchEmployee, code, name, mail, phone, idRole)
    }

    suspend fun editEmployeePass(
        id: Int,
        nameId: String,
        idBranchEmployee: Int,
        code: String,
        name: String,
        mail: String,
        password: String,
        phone: String,
        idRole: Int
    ) = safeApiCall {
        api.editEmployeePass(id, nameId, idBranchEmployee, code, name, mail,password, phone, idRole)
    }

    suspend fun getBranch(
        where: String,
        id_branch: Int
    ) = safeApiCall {
        api.getBranch(where, id_branch)
    }

    suspend fun deleteEmployee(
        id: Int,
        nameId: String
    ) = safeApiCall {
        api.deleteEmployee(id, nameId)
    }

    suspend fun getRoles(
        where: String,
        idWhere: String
    ) = safeApiCall {
        api.getRoles(where, idWhere)
    }

}