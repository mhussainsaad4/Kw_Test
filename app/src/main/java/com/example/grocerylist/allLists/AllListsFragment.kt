package com.example.grocerylist.allLists

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.allLists.adapters.AllListRecyclerAdapter
import com.example.grocerylist.allLists.viewModel.AllListsViewModel
import com.example.grocerylist.databinding.FragmentAllListsBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.utils.K.BundleConstants.Companion.LIST_SELECTED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class AllListsFragment : Fragment(), AllListRecyclerAdapter.IAllListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAllListsBinding
    private lateinit var navController: NavController
    private lateinit var entitiesList: MutableList<ListsEntity>

    @Inject
    lateinit var recyclerAdapter: AllListRecyclerAdapter

    private val allListViewModel by viewModels<AllListsViewModel>()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_lists, container, false)
        val view: View = binding.root

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllListsFragment().apply {
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
        binding.allLists = this@AllListsFragment
        navController = Navigation.findNavController(view)
        entitiesList = mutableListOf()

        initRecycler()
        defineRecycler()
        CoroutineScope(Dispatchers.Main).launch {
            collectRecyclerData()
        }
    }

    /*

    todo: recycler view          */

    private fun initRecycler() = context?.let {
        val layoutManager = LinearLayoutManager(it)
        binding.rvAllLists.layoutManager = layoutManager
    }


    private fun defineRecycler() {
        context?.let { recyclerAdapter = AllListRecyclerAdapter(it, this@AllListsFragment) }
        binding.rvAllLists.setHasFixedSize(true)
        binding.rvAllLists.adapter = recyclerAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    @ExperimentalCoroutinesApi
    private suspend fun collectRecyclerData() = allListViewModel.getAllGroceryListsRealtime().collect {
        if (it.size != 0) {
            entitiesList.addAll(it)
            recyclerAdapter.setGroceryList(it)
            recyclerAdapter.notifyDataSetChanged()
        }else showNewListLayout()
    }

    private fun showNewListLayout() {
        binding.rvAllLists.visibility = View.GONE
        binding.tvNewListLayout.visibility = View.VISIBLE
    }


    override fun onRecyclerClick(position: Int) {
        if (entitiesList.isNotEmpty()) {
            val bundle = bundleOf(LIST_SELECTED to entitiesList[position])
            navController.navigate(R.id.action_allListsFragment_to_createListsFragment, bundle)
        }
    }

    fun addGroceryListItems() = navController.navigate(R.id.action_allListsFragment_to_createListsFragment)
}