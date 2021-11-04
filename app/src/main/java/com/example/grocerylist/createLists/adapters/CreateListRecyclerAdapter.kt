package com.example.grocerylist.createLists.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.databinding.RecyclerLayoutCreateListsBinding
import com.example.grocerylist.utils.showToast
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateListRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<CreateListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = arrayListOf<String>()
    private lateinit var context: Context
    lateinit var callback: ICreateListRecyclerCallBack                        //it shouldn't be private for dependency injection
    private var isReadOnly = false

    constructor(@ActivityContext context: Context, callback: ICreateListRecyclerCallBack, isReadOnly: Boolean) : this() {
        this.context = context
        this.callback = callback
        this.isReadOnly = isReadOnly

        initCode()
    }

    private fun initCode() {
        groceryLists.clear()
        groceryLists.add(context.getString(R.string.create_list_type))
    }

    fun setGroceryList(groceryLists: ArrayList<String>) {
        this.groceryLists = groceryLists
    }

    fun getGroceryList(): ArrayList<String> {
        return this.groceryLists
    }

    fun addGroceryItem() {
        groceryLists.add("Type Here...")
        notifyItemInserted(groceryLists.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RecyclerLayoutCreateListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_create_lists, parent, false)
        val view: View = binding.root

        return MyViewHolder(view, context, binding, callback, groceryLists, isReadOnly)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.setRowData(position)
    }

    override fun getItemCount(): Int {
        return groceryLists.size
    }

    inner class MyViewHolder(private val view: View, @ActivityContext private val context: Context, private val binding: RecyclerLayoutCreateListsBinding, private val callback: ICreateListRecyclerCallBack, private val groceryLists: ArrayList<String>, private val isReadOnly: Boolean) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
            binding.allListsAdapter = this@MyViewHolder
        }

        override fun onClick(v: View?) = callback.onRecyclerClick(adapterPosition)

        fun onBindingClick() {
            showToast(context,"DataBinding Click")
        }

        fun setRowData(position: Int) {
            if (isReadOnly) {
                binding.etListItem.keyListener = null
                binding.etListItem.setText(groceryLists[position])
            }
        }
    }

    interface ICreateListRecyclerCallBack {
        fun onRecyclerClick(position: Int)
    }
}