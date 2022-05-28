package com.progdist.egm.proyectopdist.data

import android.os.Parcel
import android.os.Parcelable

data class SaleItemInfo(
    val idItem: Int,
    val idBranch: Int,
    val idCustomer: Int,
    val idUser: Int,
    val idEmployee: Int,
    val total: Float,
    val idPaymentType: Int,
    val quantity: Float

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idItem)
        parcel.writeInt(idBranch)
        parcel.writeInt(idCustomer)
        parcel.writeInt(idUser)
        parcel.writeInt(idEmployee)
        parcel.writeFloat(total)
        parcel.writeInt(idPaymentType)
        parcel.writeFloat(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SaleItemInfo> {
        override fun createFromParcel(parcel: Parcel): SaleItemInfo {
            return SaleItemInfo(parcel)
        }

        override fun newArray(size: Int): Array<SaleItemInfo?> {
            return arrayOfNulls(size)
        }
    }
}