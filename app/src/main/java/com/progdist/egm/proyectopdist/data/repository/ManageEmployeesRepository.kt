package com.progdist.egm.proyectopdist.data.repository

import com.progdist.egm.proyectopdist.data.network.ManageEmployeesApi

class ManageEmployeesRepository(
    private val api: ManageEmployeesApi
) : BaseRepository() {
}