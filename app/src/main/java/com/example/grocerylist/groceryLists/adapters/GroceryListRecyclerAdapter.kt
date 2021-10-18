package com.example.grocerylist.groceryLists.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.databinding.RecyclerLayoutGroceryListsBinding
import dagger.hilt.android.qualifiers.ActivityContext
import com.example.grocerylist.database.entity.ListsEntity
import javax.inject.Inject


class GroceryListRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<GroceryListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = mutableListOf<String>()
    private lateinit var context: Context
    lateinit var callback: IGroceryListRecyclerCallBack

    constructor(@ActivityContext context: Context, callback: IGroceryListRecyclerCallBack) : this() {
        this.context = context
        this.callback = callback
    }

    init {
        groceryLists.clear()
    }

    fun setGroceryList(groceryLists: MutableList<String>) {
        this.groceryLists = groceryLists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RecyclerLayoutGroceryListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_grocery_lists, parent, false)
        val view: View = binding.root

        return MyViewHolder(view, context, binding, callback, groceryLists)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.setRowData(position)

        //Annimation
        val animation = AnimationUtils.loadAnimation(myViewHolder.itemView.context, android.R.anim.fade_in)
        myViewHolder.itemView.animation = animation
    }

    override fun getItemCount(): Int {
        return groceryLists.size
    }

    inner class MyViewHolder(private val view: View, @ActivityContext private val context: Context, private val binding: RecyclerLayoutGroceryListsBinding, private val callback: IGroceryListRecyclerCallBack, private val groceryLists: List<String>) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            callback.onRecyclerClick(adapterPosition)
            changeBackgroundForCompletedLists(adapterPosition)
        }

        fun setRowData(position: Int) {
            binding.tvGroceryEntry.text = groceryLists[position]
        }

        private fun changeBackgroundForCompletedLists(position: Int) {
            binding.clGroceryLists.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark))
        }
    }

    interface IGroceryListRecyclerCallBack {
        fun onRecyclerClick(position: Int)
    }
}