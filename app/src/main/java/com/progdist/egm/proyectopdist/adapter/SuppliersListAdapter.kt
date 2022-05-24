package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.inventory.Supplier

class SuppliersListAdapter : RecyclerView.Adapter<SuppliersListAdapter.MyViewHolder>(){

    private var suppliersList: List<Supplier>? = null
    private lateinit var mListener: onItemClickListener
    private lateinit var mLongListener: onItemLongClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    interface onItemLongClickListener{
        fun onItemLongClick(position: Int) : Boolean
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnItemLongClickListener(listener: onItemLongClickListener){
        mLongListener = listener
    }

    fun setSuppliersList(suppliersList: List<Supplier>){
        this.suppliersList = suppliersList
    }

    fun getSupplier(position: Int): Supplier{
        return this.suppliersList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SuppliersListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.supplier_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: SuppliersListAdapter.MyViewHolder, position: Int) {
        holder.bind(suppliersList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(suppliersList == null){
            return 0
        } else {
            return suppliersList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var supplierImg: ImageView
        lateinit var supplierName: TextView
        lateinit var supplierMail: TextView
        lateinit var supplierPhone: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            supplierImg = view.findViewById(R.id.supplierImage)
            supplierName = view.findViewById(R.id.tvSupplierName)
            supplierMail = view.findViewById(R.id.tvSupplerMail)
            supplierPhone = view.findViewById(R.id.tvSupplierPhone)
        }

        fun bind(supplier: Supplier){
            supplierName.text = supplier.name_supplier
            supplierMail.text = supplier.mail_supplier
            supplierPhone.text = supplier.phone_number_supplier
        }
    }


}