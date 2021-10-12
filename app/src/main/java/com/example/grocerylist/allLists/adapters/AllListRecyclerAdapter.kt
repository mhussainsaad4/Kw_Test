package com.example.grocerylist.allLists.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.allLists.callbacks.IAllListRecyclerCallBack
import com.example.grocerylist.databinding.RecyclerLayoutAllListsBinding
import org.dropby.app.database.contacts.ListsEntity


class AllListRecyclerAdapter(private val context: Context, private val callback: IAllListRecyclerCallBack) : RecyclerView.Adapter<AllListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = mutableListOf<ListsEntity>()

    init {
        groceryLists.clear()
    }

    fun setGroceryList(groceryLists: MutableList<ListsEntity>) {
        this.groceryLists = groceryLists
    }

    fun addGroceryListItem(entity: ListsEntity) {
        groceryLists.add(entity)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding:RecyclerLayoutAllListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_all_lists, parent, false)
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

    inner class MyViewHolder(private val view: View, private val context: Context, private val binding: RecyclerLayoutAllListsBinding, private val callback: IAllListRecyclerCallBack, private val groceryLists: List<ListsEntity>) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) = callback.onRecyclerClick(adapterPosition)

        fun setRowData(position: Int) {

        }
    }
}