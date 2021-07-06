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


        goToExpense.setOnClickListener{
            this.findNavController().navigate(R.id.action_landInFragment_to_expenseListFragment)
        }

        return view
    }

}