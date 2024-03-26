package com.example.apiimplementation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiimplementation.databinding.FragmentTittleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Tittle : Fragment() {
lateinit var binding :FragmentTittleBinding
lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tittle,container,false)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
        val apiData = retrofitBuilder.getData()
        apiData.enqueue(object : Callback<ProductsData?> {
            override fun onResponse(call: Call<ProductsData?>, response: Response<ProductsData?>) {
                val dataResponse = response.body()
                val dataList = dataResponse?.products
                val dataString = StringBuilder()
                val tittleData = dataList?.let { Array<String>(it.size){""} }
                if(dataList != null) {
                    for ((i, data) in dataList.withIndex()) {
                        tittleData?.set(i, data.title)
                    }
                }
                Toast.makeText(context,"Data Loaded", Toast.LENGTH_SHORT).show()
                recyclerView = binding.recyclerViewForData
                recyclerView.layoutManager = LinearLayoutManager(context)
                val adapter = tittleData?.let { Adapter(it) }
                recyclerView.adapter = adapter

            }

            override fun onFailure(call: Call<ProductsData?>, t: Throwable) {
              Log.d("Main Activity", "Failure" +t.message)
                Toast.makeText(context,"Failure", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root

    }

}