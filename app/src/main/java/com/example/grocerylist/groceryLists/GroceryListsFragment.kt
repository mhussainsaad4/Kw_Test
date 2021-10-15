package com.example.grocerylist.groceryLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.databinding.FragmentGroceryListsBinding
import com.example.grocerylist.groceryLists.adapters.GroceryListRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class GroceryListsFragment : Fragment(), GroceryListRecyclerAdapter.IGroceryListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGroceryListsBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var recyclerAdapter: GroceryListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        inflater.inflate(R.layout.fragment_grocery_lists, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grocery_lists, container, false)
        val view: View = binding.root

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroceryListsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCode(view)
    }

    private fun initCode(view: View) {
        binding.lifecycleOwner = this
        binding.grocery = this@GroceryListsFragment
        navController = Navigation.findNavController(view)

        initRecycler()
        defineRecycler()
    }

    /*

    todo: recycler view          */

    private fun initRecycler() {
        context?.let {
            val layoutManager = LinearLayoutManager(it)
            binding.rvGroceryLists.layoutManager = layoutManager
        }
    }

    private fun defineRecycler() {
        context?.let {
            recyclerAdapter = GroceryListRecyclerAdapter(it, this@GroceryListsFragment)
        }
        binding.rvGroceryLists.setHasFixedSize(true)
        binding.rvGroceryLists.adapter = recyclerAdapter
    }

    override fun onRecyclerClick(position: Int) {

    }
}