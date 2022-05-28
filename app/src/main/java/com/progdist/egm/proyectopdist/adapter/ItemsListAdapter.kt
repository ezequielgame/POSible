package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.inventory.Item

class ItemsListAdapter : RecyclerView.Adapter<ItemsListAdapter.MyViewHolder>(){

    private var itemsList: List<Item>? = null
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

    fun setItemsList(itemsList: List<Item>){
        this.itemsList = itemsList
    }


    fun getItem(position: Int): Item{
        return this.itemsList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemsListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: ItemsListAdapter.MyViewHolder, position: Int) {
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
        lateinit var itemName: TextView
        lateinit var itemPrice: TextView
        lateinit var itemStock: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            itemImg = view.findViewById(R.id.itemImage)
            itemName = view.findViewById(R.id.tvItemName)
            itemPrice = view.findViewById(R.id.tvItemPrice)
            itemStock = view.findViewById(R.id.tvItemStock)
        }

        fun bind(item: Item){
            itemName.text = item.name_item
            itemPrice.text = "Precio: ${item.sale_price_item}"
            itemStock.text = "Cantidad: ${item.stock_item}"
        }
    }


}