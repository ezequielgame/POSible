package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.sales.Sale

class SalesListItemsListAdapter() : RecyclerView.Adapter<SalesListItemsListAdapter.MyViewHolder>(){

    private var itemsList: List<Sale>? = ArrayList()
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

    fun setItemsList(itemsList: List<Sale>){
        this.itemsList = itemsList
    }

    fun getItemsList(): List<Sale>? {
        return this.itemsList
    }


    fun getItem(position: Int): Sale{
        return this.itemsList?.get(position)!!
    }

    fun addItem(item: Sale){
        this.itemsList = this.itemsList?.plus(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SalesListItemsListAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sales_list_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: SalesListItemsListAdapter.MyViewHolder, position: Int) {
        holder.bind(itemsList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(itemsList == null){
            return 0
        } else {
            return itemsList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var itemImg: ImageView
        lateinit var saleName: TextView
        lateinit var saleDate: TextView
        lateinit var saleQuantity: TextView
        lateinit var saleTotal: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            itemImg = view.findViewById(R.id.saleImage)
            saleName = view.findViewById(R.id.tvSaleName)
            saleDate = view.findViewById(R.id.tvSaleDate)
            saleQuantity = view.findViewById(R.id.tvSaleQuantity)
            saleTotal = view.findViewById(R.id.tvSaleTotal)
        }

        fun bind(item: Sale){
            saleName.text = "Venta #" + (getItemsList()!!.indexOf(item)+1).toString()
            saleDate.text = "Fecha: " + item.date_created_sale
            saleQuantity.text = "# Productos: " + item.total_quantity_sale.toString()
            saleTotal.text = "$${item.total_sale}"
        }

    }

}