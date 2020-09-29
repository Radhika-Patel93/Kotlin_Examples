package com.demo.kotlinexamples.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.kotlinexamples.R
import com.demo.kotlinexamples.adapter.Adapter
import com.demo.kotlinexamples.model.Model
import kotlinx.android.synthetic.main.activity_recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class RecyclerViewActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        setToolbar()
        setAdapter()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_include.toolbar)
        toolbar_include.toolbar.setTitle("")
        toolbar_include.txtviewTitle.text = "Kotlin Examples"
    }

    private fun setAdapter(){

        val list = ArrayList<Model>();
        list.add(Model("One",R.mipmap.ic_launcher))
        list.add(Model("Two",R.mipmap.ic_launcher))
        list.add(Model("Three",R.mipmap.ic_launcher))
        list.add(Model("Four",R.mipmap.ic_launcher))
        list.add(Model("Five",R.mipmap.ic_launcher))
        list.add(Model("Six",R.mipmap.ic_launcher))
        list.add(Model("Seven",R.mipmap.ic_launcher))
        list.add(Model("Eight",R.mipmap.ic_launcher))
        list.add(Model("Nine",R.mipmap.ic_launcher))
        list.add(Model("Ten",R.mipmap.ic_launcher))

        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerview.adapter = Adapter(this, list)

    }
}