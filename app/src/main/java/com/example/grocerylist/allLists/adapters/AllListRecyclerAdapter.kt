package com.example.grocerylist.allLists.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.grocerylist.R
import com.example.grocerylist.databinding.RecyclerLayoutAllListsBinding
import dagger.hilt.android.qualifiers.ActivityContext
import com.example.grocerylist.database.lists.ListsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllListRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<AllListRecyclerAdapter.MyViewHolder>() {

    private var groceryLists = mutableListOf<ListsEntity>()
    private lateinit var context: Context
    lateinit var callback: IAllListRecyclerCallBack             //it shouldn't be private for dependency injection

    constructor(@ActivityContext context: Context, callback: IAllListRecyclerCallBack) : this() {
        this.context = context
        this.callback = callback
    }

    init {
        groceryLists.clear()
        val entity = ListsEntity()
        groceryLists.add(entity)
        groceryLists.add(entity)
        groceryLists.add(entity)
        groceryLists.add(entity)

    }

    fun setGroceryList(groceryLists: MutableList<ListsEntity>) {
        this.groceryLists = groceryLists
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addGroceryListItem(entity: ListsEntity) {
        groceryLists.add(entity)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: RecyclerLayoutAllListsBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_layout_all_lists, parent, false)
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

    inner class MyViewHolder(private val view: View, @ActivityContext private val context: Context, private val binding: RecyclerLayoutAllListsBinding, private val callback: IAllListRecyclerCallBack, private val groceryLists: List<ListsEntity>) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) = callback.onRecyclerClick(adapterPosition)

        fun setRowData(position: Int) {

        }
    }

    interface IAllListRecyclerCallBack {
        fun onRecyclerClick(position: Int)
    }
}