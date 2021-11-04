package com.example.grocerylist.createLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylist.R
import com.example.grocerylist.createLists.adapters.CreateListRecyclerAdapter
import com.example.grocerylist.createLists.viewModel.CreateListsViewModel
import com.example.grocerylist.database.entity.ListsEntity
import com.example.grocerylist.databinding.FragmentCreateListsBinding
import com.example.grocerylist.utils.K
import com.example.grocerylist.utils.K.BundleConstants.Companion.LIST_SELECTED
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
    private var selectedEntity: ListsEntity? = null

    private lateinit var binding: FragmentCreateListsBinding
    private lateinit var navController: NavController
    private val createListsViewModel by viewModels<CreateListsViewModel>()

    @Inject
    lateinit var recyclerAdapter: CreateListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            selectedEntity = it.getParcelable(LIST_SELECTED)
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

        updateUI()
        initRecycler()
    }

    private fun updateUI() = if (selectedEntity != null) {
        binding.etListName.keyListener = null
        selectedEntity?.listName.let { binding.etListName.setText(it) }
        defineRecycler(true)
        setRecyclerList()
    } else {
        binding.floatingActionButton.visibility = View.VISIBLE
        showSavingToast()
        enableOnBackPress()
        defineRecycler(false)
    }

    private fun showSavingToast() = context?.let { showToastLong(it, it.getString(R.string.create_list_toast_back)) }

    /*

    todo: recycler view          */

    private fun initRecycler() = context?.let {
        val layoutManager = LinearLayoutManager(it)
        binding.rvCreateLists.layoutManager = layoutManager
    }

    private fun defineRecycler(isReadOnly: Boolean) {
        context?.let { recyclerAdapter = CreateListRecyclerAdapter(it, this@CreateListsFragment, isReadOnly) }
        binding.rvCreateLists.setHasFixedSize(true)
        binding.rvCreateLists.adapter = recyclerAdapter
    }

    fun addGroceryItems() = recyclerAdapter.addGroceryItem()

    private fun setRecyclerList() = selectedEntity?.listEntries?.let {
        val groceryLists = arrayListOf<String>()
        groceryLists.addAll(it)
        recyclerAdapter.setGroceryList(groceryLists)
    }


    override fun onRecyclerClick(position: Int) {

    }

    /*

    todo: save list to room          */

    private fun enableOnBackPress() = context?.let {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            getAllRowsFromRecyclerView()
            isEnabled = false                       //disable on back press callback for next time
        }
        callback.isEnabled = true

    }


    private fun getAllRowsFromRecyclerView() {
        val size = recyclerAdapter.getGroceryList().size        //Get All Rows data from recycler view
        val groceryLists = mutableListOf<String>()

        for (index in 0 until size) {
            val row: View? = binding.rvCreateLists.layoutManager?.findViewByPosition(index)         //get row by position
            val rowText = row?.findViewById<EditText>(R.id.etListItem)?.text.toString()             //get row text
            groceryLists.add(rowText)
        }

        //check for empty list
        checkForEmptyList(groceryLists)
    }

    private fun checkForEmptyList(groceryLists: MutableList<String>) {
        if (groceryLists.size == 1 && groceryLists[0].isEmpty()) {
            context?.let { showToast(it, it.getString(R.string.create_list_toast_empty)) }
            goBack()
        } else populateEntity(groceryLists)
    }

    private fun populateEntity(groceryLists: MutableList<String>) {
        val listName = if (binding.etListName.text.isEmpty()) getString(R.string.create_list_heading)
        else binding.etListName.text.toString()

        val listsEntity = ListsEntity(listName, groceryLists, K.Constants.STATUS_PENDING)

        CoroutineScope(Dispatchers.Main).launch { createListsViewModel.insertNewListToDb(listsEntity) }
        context?.let { showToast(it, it.getString(R.string.create_list_toast_saved)) }
        goBack()
    }

    private fun goBack() {
        navController.popBackStack()                    //to go back
    }
}