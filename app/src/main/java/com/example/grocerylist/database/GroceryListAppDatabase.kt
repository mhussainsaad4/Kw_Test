package com.example.grocerylist.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.grocerylist.application.GroceryListApplication
import com.example.grocerylist.database.converter.ListConverters
import com.example.grocerylist.database.dao.ListsDataAccessObjectDao
import com.example.grocerylist.database.entity.ListsEntity

@Database(entities = arrayOf(ListsEntity::class), version = 1, exportSchema = false)               //export schema don't keep instance of older versions in memory
@TypeConverters(ListConverters::class)
abstract class GroceryListAppDatabase : RoomDatabase() {

    abstract fun groceryListsDao(): ListsDataAccessObjectDao

    companion object {                                                                  // Singleton prevents multiple instances of database opening at the same time.

        @Volatile
        private var dbInstance: GroceryListAppDatabase? = null

        fun getAppDatabase(): GroceryListAppDatabase {

            return dbInstance ?: synchronized(this) {               // if the INSTANCE is not null, then return it, if it is, then create the database
                val instance = Room.databaseBuilder(GroceryListApplication.appContext, GroceryListAppDatabase::class.java, "grocery_list_database").build()
                dbInstance = instance           // return instance
                instance
            }
        }
    }
}