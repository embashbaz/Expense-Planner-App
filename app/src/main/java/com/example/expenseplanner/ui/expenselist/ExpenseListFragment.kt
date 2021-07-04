package com.example.expenseplanner.ui.expenselist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.R
import com.example.expenseplanner.ui.dialogs.NewCartDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ExpenseListFragment : Fragment() {

    lateinit var expenseListRecycler: RecyclerView
    lateinit var addCartFloatingActionButton: FloatingActionButton
    lateinit var expenseListViewModel: ExpenseListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_expense_list, container, false)
        addCartFloatingActionButton = view.findViewById(R.id.add_cart_button)


        addCartFloatingActionButton.setOnClickListener {
            createCart()
        }

        return  view;
    }

    private fun createCart() {
        val newCartDialog = NewCartDialog(expenseListViewModel)
        newCartDialog.show(parentFragmentManager, "create new Cart")
    }


}