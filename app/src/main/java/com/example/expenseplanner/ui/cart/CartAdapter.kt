package com.example.expenseplanner.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.R
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.ui.expenselist.ExpenseListAdapter

class CartAdapter (onClick: (ItemProduct) -> Unit): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<ItemProduct>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount()= allItems.size

    fun setData(items: ArrayList<ItemProduct>){
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (ItemProduct) -> Unit) : RecyclerView.ViewHolder(itemView){

        lateinit var mItem: ItemProduct
        init{
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }
        fun bind(item: ItemProduct){
            mItem = item



        }

        companion object {
            fun from(parent: ViewGroup, onClick: (ItemProduct) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                 val view = layoutInflater
                   .inflate(R.layout.cart_item, parent, false)

                  return ViewHolder(view, onClick)
            }

        }

    }


}