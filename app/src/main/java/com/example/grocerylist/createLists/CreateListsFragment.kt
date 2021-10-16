package com.example.grocerylist.createLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.createLists.adapters.CreateListRecyclerAdapter
import com.example.grocerylist.createLists.viewModel.CreateListsViewModel
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.databinding.FragmentCreateListsBinding
import com.example.grocerylist.utils.K
import com.example.grocerylist.utils.showToast
import com.example.grocerylist.utils.showToastLong
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CreateListsFragment : Fragment(), CreateListRecyclerAdapter.ICreateListRecyclerCallBack {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCreateListsBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var recyclerAdapter: CreateListRecyclerAdapter

    private val createListsViewModel by viewModels<CreateListsViewModel>()

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
//        createListsViewModel = ViewModelProvider(this@CreateListsFragment).get(CreateListsViewModel::class.java)

        initRecycler()
        defineRecycler()
        showSavingToast()
        enableOnBackPress()
    }

    private fun showSavingToast() = context?.let { showToastLong(it, it.getString(R.string.create_list_toast_back)) }

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

    fun addGroceryItems() = recyclerAdapter.addGroceryItem()

    override fun onRecyclerClick(position: Int) {

    }

    /*

    todo: save list to room          */

    private fun enableOnBackPress() {
        context?.let {
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    populateEntity()
                }
            })
        }
    }

    private fun populateEntity() {
        val listName = if (binding.etListName.text.isNotEmpty()) binding.etListName.text.toString()
        else getString(R.string.create_list_heading)

        //Get All Rows data from recycler view
        val size = recyclerAdapter.getGroceryList().size
        val groceryLists = mutableListOf<String>()

        for (index in 0 until size) {
            val row: View? = binding.rvCreateLists.layoutManager?.findViewByPosition(index)
            val rowText = row?.findViewById<EditText>(R.id.etListItem)?.text.toString()
            groceryLists.add(rowText)
        }

        val listsEntity = ListsEntity(listName, groceryLists, K.Constants.STATUS_PENDING)
        CoroutineScope(Dispatchers.Main).launch {
            createListsViewModel.insertNewListToDb(listsEntity)
        }

        context?.let { showToast(it, it.getString(R.string.create_list_toast_saved)) }
        navController.popBackStack()                    //to go back
    }
}