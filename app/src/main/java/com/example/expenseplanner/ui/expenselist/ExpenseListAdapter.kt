package com.example.expenseplanner.ui.expenselist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct

class ExpenseListAdapter(onClick: (Cart) -> Unit): RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Cart>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount()= allItems.size

    fun setData(items: ArrayList<Cart>){
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View, onClick: (Cart) -> Unit) : RecyclerView.ViewHolder(itemView){

        lateinit var mItem: Cart
        init{
            itemView.setOnClickListener {
                onClick(mItem)
            }
        }
        fun bind(item: Cart){
            mItem = item





        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Cart) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
               // val view = layoutInflater
                 //   .inflate(R.layout.booking_list_item, parent, false)

              //  return ViewHolder(view, onClick)
            }

        }

    }
}