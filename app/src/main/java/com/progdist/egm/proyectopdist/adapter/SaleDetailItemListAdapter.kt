package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.sales.SaleItem

class SaleDetailItemListAdapter() : RecyclerView.Adapter<SaleDetailItemListAdapter.MyViewHolder>(){

    private var itemsList: List<SaleItem>? = ArrayList()
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

    fun setItemsList(itemsList: List<SaleItem>){
        this.itemsList = itemsList
    }

    fun getItemsList(): List<SaleItem>? {
        return this.itemsList
    }


    fun getItem(position: Int): SaleItem{
        return this.itemsList?.get(position)!!
    }

    fun addItem(item: SaleItem){
        this.itemsList = this.itemsList?.plus(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SaleDetailItemListAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sale_summary_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: SaleDetailItemListAdapter.MyViewHolder, position: Int) {
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
        lateinit var itemName: TextView
        lateinit var itemPrice: TextView
        lateinit var itemQuantity: TextView
        lateinit var itemTotal: TextView


        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            itemName = view.findViewById(R.id.tvItemName)
            itemPrice = view.findViewById(R.id.tvItemPrice)
            itemQuantity = view.findViewById(R.id.tvItemQuantity)
            itemTotal = view.findViewById(R.id.tvItemTotal)

        }
        fun bind(item: SaleItem){
            itemName.text = item.name_item
            itemPrice.text = "$${item.sale_price_item}"
            itemQuantity.text = item.quantity_sale_item.toString()
            itemTotal.text = "$${item.quantity_sale_item * item.sale_price_item}"
        }

    }

}