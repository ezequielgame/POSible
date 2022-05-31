package com.progdist.egm.proyectopdist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.progdist.egm.proyectopdist.data.network.Resource
import com.progdist.egm.proyectopdist.data.repository.SalesRepository
import com.progdist.egm.proyectopdist.data.responses.generic.EditResponse
import com.progdist.egm.proyectopdist.domain.EditCode
import kotlinx.coroutines.launch

class CodeScannerViewModel(
    private val repository: SalesRepository
) : ViewModel() {

    private val _editCodeResponse: MutableLiveData<Resource<EditResponse>> = MutableLiveData()
    val editCodeResponse: LiveData<Resource<EditResponse>> get()= _editCodeResponse

    fun editCode(
    id: Int,
    nameId: String,
    code: String
    ) = viewModelScope.launch {
        val editCodeUseCase = EditCode()
        _editCodeResponse.value = (editCodeUseCase(repository, id, nameId, code) as Resource<EditResponse>)
    }

}