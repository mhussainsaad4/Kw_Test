package com.example.grocerylist.groceryLists.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.databinding.RecyclerLayoutGroceryListsBinding
import com.example.grocerylist.groceryLists.callbacks.IGroceryListRecyclerCallBack
import org.dropby.app.database.contacts.ListsEntity


class GroceryListRecyclerAdapter(private val context: Context, private val callback: IGroceryListRecyclerCallBack) : RecyclerView.Adapter<GroceryListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = mutableListOf<ListsEntity>()

    init {
        groceryLists.clear()
    }

    fun setGroceryList(groceryLists: MutableList<ListsEntity>) {
        this.groceryLists = groceryLists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding:RecyclerLayoutGroceryListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_grocery_lists, parent, false)
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
        return 4
    }

    inner class MyViewHolder(private val view: View, private val context: Context, private val binding: RecyclerLayoutGroceryListsBinding, private val callback: IGroceryListRecyclerCallBack, private val groceryLists: List<ListsEntity>) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) = callback.onRecyclerClick(adapterPosition)

        fun setRowData(position: Int) {

        }
    }
}