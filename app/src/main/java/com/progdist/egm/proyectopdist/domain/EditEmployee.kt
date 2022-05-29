package com.progdist.egm.proyectopdist.domain

import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.InventoryRepository
import com.progdist.egm.proyectopdist.data.repository.ManageEmployeesRepository

class EditEmployee {

    suspend operator fun invoke(repository: ManageEmployeesRepository,
                                id: Int,
                                nameId: String,
                                idBranchEmployee: Int,
                                code: String,
                                name: String,
                                mail: String,
                                phone: String,
                                idRole: Int): Resource<Any> =
        repository.editEmployee(id, nameId, idBranchEmployee, code, name, mail, phone, idRole)

}