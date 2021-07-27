package com.example.expenseplanner.ui.expenselist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart

class ExpenseListAdapter(onClick: (Cart) -> Unit): RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Cart>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = allItems.get(position)
        if (item != null) {
            holder.bind(item)
        }
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

            val expenseType = itemView.findViewById<TextView>(R.id.type_expense_list)
            val cartStatus = itemView.findViewById<TextView>(R.id.status_expense_list)
            val cartTotalprice = itemView.findViewById<TextView>(R.id.price_expense_list)

            expenseType.setText(item.type)
            cartStatus.setText(item.status.toString())
            cartTotalprice.setText(item.totalPrice.toString())



        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Cart) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.expense_item, parent, false)
               return ViewHolder(view, onClick)
            }

        }

    }
}