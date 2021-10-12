package com.example.grocerylist.createLists.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.createLists.callbacks.ICreateListRecyclerCallBack
import com.example.grocerylist.databinding.RecyclerLayoutCreateListsBinding
import org.dropby.app.database.contacts.ListsEntity


class CreateListRecyclerAdapter(private val context: Context, private val callback: ICreateListRecyclerCallBack) : RecyclerView.Adapter<CreateListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = mutableListOf<String>()

    init {
        groceryLists.clear()
        groceryLists.add("ketchup")
        groceryLists.add("rice")
        groceryLists.add("Type Here...")
    }

    fun setGroceryList(groceryLists: MutableList<String>) {
        this.groceryLists = groceryLists
    }

    fun addGroceryItem() {
        groceryLists.add("Type Here...")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RecyclerLayoutCreateListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_create_lists, parent, false)
        val view: View = binding.root

        return MyViewHolder(view, context, binding, callback, groceryLists)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.setRowData(position)
    }

    override fun getItemCount(): Int {
        return groceryLists.size
    }

    inner class MyViewHolder(private val view: View, private val context: Context, private val binding: RecyclerLayoutCreateListsBinding, private val callback: ICreateListRecyclerCallBack, private val groceryLists: List<String>) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) = callback.onRecyclerClick(adapterPosition)

        fun setRowData(position: Int) {
            binding.etListItems.setText(groceryLists.get(position))
        }
    }
}