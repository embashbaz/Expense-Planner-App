package com.example.expenseplanner.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.ui.expenselist.ExpenseListAdapter

class CartAdapter (onClick: (ItemProduct) -> Unit): RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<ItemProduct>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenseListAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ExpenseListAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
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
                // val view = layoutInflater
                //   .inflate(R.layout.booking_list_item, parent, false)

                //  return ViewHolder(view, onClick)
            }

        }

    }


}