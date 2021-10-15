package com.example.grocerylist.groceryLists.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GroceryListsRepository @Inject constructor(@ApplicationContext val context: Context) {
}