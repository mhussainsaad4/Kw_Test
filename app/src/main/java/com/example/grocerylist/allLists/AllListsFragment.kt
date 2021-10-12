package com.example.grocerylist.allLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.allLists.adapters.AllListRecyclerAdapter
import com.example.grocerylist.allLists.callbacks.IAllListRecyclerCallBack
import com.example.grocerylist.databinding.FragmentAllListsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.dropby.app.database.contacts.ListsEntity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AllListsFragment : Fragment(), View.OnClickListener, IAllListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAllListsBinding
    private lateinit var navController: NavController
    private lateinit var recyclerAdapter: AllListRecyclerAdapter

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
        binding.allLists = AllListsFragment()
        navController = Navigation.findNavController(view)
        binding.floatingActionButton.setOnClickListener(this)

        initRecycler()
        defineRecycler()
    }

    override fun onClick(v: View?) {
        if (v == binding.floatingActionButton)
            addGroceryListItems()
    }

    /*

    todo: recycler view          */

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvAllLists.layoutManager = layoutManager
    }

    private fun defineRecycler() {
        recyclerAdapter = AllListRecyclerAdapter(context!!, this)
        binding.rvAllLists.setHasFixedSize(true)
        binding.rvAllLists.adapter = recyclerAdapter
    }

    override fun onRecyclerClick(position: Int) {
        val bundle = bundleOf("listSelected" to position)
        navController.navigate(R.id.action_allListsFragment_to_createListsFragment, bundle)
    }

    private fun addGroceryListItems() {
        recyclerAdapter.addGroceryListItem(ListsEntity())
    }
}