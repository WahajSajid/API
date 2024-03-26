package com.example.apiimplementation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiimplementation.databinding.FragmentDescriptionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Description : Fragment() {
    lateinit var binding: FragmentDescriptionBinding
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_description,container,false)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)

        val dataFromAPI = retrofitBuilder.getData()
        dataFromAPI.enqueue(object : Callback<ProductsData?> {
            override fun onResponse(call: Call<ProductsData?>, response: Response<ProductsData?>) {
                val dataResponse = response.body()
                val dataList = dataResponse?.products
                val descriptionData = dataList?.let { Array<String>(it.size){""} }
                if(dataList != null){
                    for((i,data) in dataList.withIndex()){
                        descriptionData?.set(i, data.description)
                    }
                    recyclerView = binding.descriptionRecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    val adapter = descriptionData?.let { DescriptionAdapter(it) }
                    recyclerView.adapter = adapter
                }

            }

            override fun onFailure(call: Call<ProductsData?>, t: Throwable) {
               Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show()
            }
        })
        return binding.root
    }

}