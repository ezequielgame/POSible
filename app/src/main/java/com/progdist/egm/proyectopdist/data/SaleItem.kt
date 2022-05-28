package com.progdist.egm.proyectopdist.data

import com.progdist.egm.proyectopdist.data.responses.inventory.Item

data class SaleItem(
    val item: Item,
    val qty: Int
)