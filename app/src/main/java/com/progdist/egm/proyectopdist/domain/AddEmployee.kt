package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository

class AddEmployee {

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                idUserEmployee: Int,
                                idBranchEmployee: Int,
                                code: String,
                                name: String,
                                mail: String,
                                password: String,
                                phone: String,
                                idRole: Int): Resource<Any> =
        repository.addEmployee(idUserEmployee, idBranchEmployee, code, name, mail, password, phone, idRole)

}