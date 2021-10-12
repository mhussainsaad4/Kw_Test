package com.example.grocerylist.utils


class Functions private constructor() {

    companion object {
        private val functions = Functions()

        @Synchronized
        fun getInstance(): Functions {
            return functions
        }
    }

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    fun getRetrofitInstance(): Retrofit {
//        return retrofit
//    }
}