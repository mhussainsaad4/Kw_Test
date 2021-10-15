package com.example.grocerylist.createLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.createLists.adapters.CreateListRecyclerAdapter
import com.example.grocerylist.databinding.FragmentCreateListsBinding
import com.example.grocerylist.utils.showToast
import com.example.grocerylist.utils.showToastLong
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CreateListsFragment : Fragment(), View.OnClickListener, CreateListRecyclerAdapter.ICreateListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCreateListsBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var recyclerAdapter: CreateListRecyclerAdapter

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_lists, container, false)
        val view: View = binding.root

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateListsFragment().apply {
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
        binding.createLists = this@CreateListsFragment
        navController = Navigation.findNavController(view)
        binding.floatingActionButton.setOnClickListener(this)

        initRecycler()
        defineRecycler()
        showSavingToast()
        enableOnBackPress()
    }

    private fun showSavingToast() = context?.let { showToastLong(it, "Press Back Button to Save the List") }

    override fun onClick(v: View?) {
        if (v == binding.floatingActionButton)
            addGroceryItems()
    }

    /*

    todo: recycler view          */

    private fun initRecycler() =
        context?.let {
            val layoutManager = LinearLayoutManager(it)
            binding.rvCreateLists.layoutManager = layoutManager
        }

    private fun defineRecycler() {
        context?.let {
            recyclerAdapter = CreateListRecyclerAdapter(it, this@CreateListsFragment)
        }
        binding.rvCreateLists.setHasFixedSize(true)
        binding.rvCreateLists.adapter = recyclerAdapter
    }


    private fun addGroceryItems() = recyclerAdapter.addGroceryItem()

    override fun onRecyclerClick(position: Int) {

    }

    /*

    todo: save list to room          */

    private fun enableOnBackPress() {
        context?.let {
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    
                    context?.let { showToast(it, "Saved") }
                    navController.popBackStack()
                }
            })
        }
    }
}