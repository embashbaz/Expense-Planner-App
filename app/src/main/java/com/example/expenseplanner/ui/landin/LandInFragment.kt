package com.example.expenseplanner.ui.landin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.expenseplanner.R


class LandInFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_land_in, container, false)
        val goToExpense = view.findViewById<CardView>(R.id.go_expense_card)
        val goToMap = view.findViewById<CardView>(R.id.go_to_shops_map)
        val goToOrders = view.findViewById<CardView>(R.id.go_to_orders)


        goToExpense.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("code", 1)
            this.findNavController().navigate(R.id.action_landInFragment_to_expenseListFragment, bundle)
        }

        goToMap.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("order_passed", 2)
            this.findNavController().navigate(R.id.action_landInFragment_to_mapsFragment, bundle)
        }

        goToOrders.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("code", 2)
            this.findNavController().navigate(R.id.action_landInFragment_to_expenseListFragment, bundle)

        }

        return view
    }

}