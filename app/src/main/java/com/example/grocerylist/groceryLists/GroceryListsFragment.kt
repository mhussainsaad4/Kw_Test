package com.example.grocerylist.groceryLists

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.databinding.FragmentGroceryListsBinding
import com.example.grocerylist.groceryLists.adapters.GroceryListRecyclerAdapter
import com.example.grocerylist.groceryLists.viewModel.GroceryListsViewModel
import com.example.grocerylist.utils.K.Constants.Companion.STATUS_COMPLETED
import com.example.grocerylist.utils.K.Constants.Companion.STATUS_PENDING
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class GroceryListsFragment : Fragment(), GroceryListRecyclerAdapter.IGroceryListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGroceryListsBinding
    private lateinit var navController: NavController
    private val groceryListsViewModel by viewModels<GroceryListsViewModel>()
    private lateinit var fragmentView: View

    private lateinit var currentListsEntity: ListsEntity
    private lateinit var groceryEntitiesList: MutableList<String>
    private lateinit var groceryListHashMap: HashMap<String, String>

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
        private var entriesCount = 0

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
        fragmentView = view

        initCode()
    }

    private fun initCode() {
        binding.lifecycleOwner = this@GroceryListsFragment
        binding.grocery = this@GroceryListsFragment
        navController = Navigation.findNavController(fragmentView)
        currentListsEntity = ListsEntity()
        groceryListHashMap = HashMap()
        groceryEntitiesList = mutableListOf()
        entriesCount = 0

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
        binding.rvGroceryLists.layoutManager = layoutManager
    }


    private fun defineRecycler() {
        context?.let { recyclerAdapter = GroceryListRecyclerAdapter(it, this@GroceryListsFragment) }
        binding.rvGroceryLists.setHasFixedSize(true)
        binding.rvGroceryLists.adapter = recyclerAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    @ExperimentalCoroutinesApi
    private suspend fun collectRecyclerData() = groceryListsViewModel.getLastPendingList().collect {
        it.listName?.let { listName ->
            if (listName.isNotEmpty())
                it.listEntries?.let { groceryEntries ->
                    currentListsEntity = it
                    groceryEntitiesList.addAll(groceryEntries)
                    recyclerAdapter.setGroceryList(groceryEntries)
                    recyclerAdapter.notifyDataSetChanged()
                    populateHashmap(groceryEntries)
                }
            else showNewListLayout()
        }
    }

    private fun showNewListLayout() {
        binding.rvGroceryLists.visibility = View.GONE
        binding.tvNewListLayout.visibility = View.VISIBLE
    }

    private fun populateHashmap(groceryEntries: MutableList<String>) {
        for (item in groceryEntries) groceryListHashMap[item] = STATUS_PENDING
    }

    override fun onRecyclerClick(position: Int) {
        if (groceryListHashMap[groceryEntitiesList[position]] == STATUS_PENDING) {
            groceryListHashMap[groceryEntitiesList[position]] = STATUS_COMPLETED
            ++entriesCount
            if (entriesCount == groceryEntitiesList.size) updateCompletedListStatusInDatabase()
        }
    }

    /*

    todo: recycler view          */

    @ExperimentalCoroutinesApi
    private fun updateCompletedListStatusInDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            groceryListsViewModel.updateCompletedListStatus(currentListsEntity.listId.toString()).collect { updatedRows ->
                if (updatedRows > 0)
                    initCode()
            }
        }
    }
}