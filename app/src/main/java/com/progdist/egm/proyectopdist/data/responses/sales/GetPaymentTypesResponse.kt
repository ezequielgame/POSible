package com.progdist.egm.proyectopdist.data.responses.sales

data class GetPaymentTypesResponse(
    val result: List<PaymentType>,
    val status: Int,
    val total: Int
)