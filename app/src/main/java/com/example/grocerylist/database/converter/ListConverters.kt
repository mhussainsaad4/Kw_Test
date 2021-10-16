package com.example.grocerylist.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListConverters {

    @TypeConverter
    fun listToJsonString(value: MutableList<String>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toMutableList()

}