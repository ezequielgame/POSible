package com.progdist.egm.proyectopdist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.progdist.egm.proyectopdist.R
import com.progdist.egm.proyectopdist.data.responses.inventory.Category

class CategoriesListAdapter : RecyclerView.Adapter<CategoriesListAdapter.MyViewHolder>(){

    private var categoriesList: List<Category>? = null
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

    fun setCategoriesList(categoriesList: List<Category>){
        this.categoriesList = categoriesList
    }


    fun getCategory(position: Int): Category{
        return this.categoriesList?.get(position)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategoriesListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return MyViewHolder(view, mListener, mLongListener)
    }

    override fun onBindViewHolder(holder: CategoriesListAdapter.MyViewHolder, position: Int) {
        holder.bind(categoriesList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if(categoriesList == null){
            return 0
        } else {
            return categoriesList?.size!!
        }
    }

    inner class MyViewHolder(view: View, listener: onItemClickListener, longListener: onItemLongClickListener): RecyclerView.ViewHolder(view){

        lateinit var categoryImg: ImageView
        lateinit var categoryName: TextView
        lateinit var categoryDetail: TextView

        init {
            view.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            view.setOnLongClickListener {
                longListener.onItemLongClick(adapterPosition)
            }
            categoryImg = view.findViewById(R.id.categoryImage)
            categoryName = view.findViewById(R.id.tvCateogryName)
            categoryDetail = view.findViewById(R.id.tvCategoryDetail)
        }

        fun bind(category: Category){
            categoryName.text = category.name_category
            categoryDetail.text = category.description_category
        }
    }


}