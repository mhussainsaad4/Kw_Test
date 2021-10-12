package com.example.grocerylist.groceryLists.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroceryListsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

}