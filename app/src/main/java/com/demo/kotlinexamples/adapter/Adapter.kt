package com.demo.kotlinexamples.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.demo.kotlinexamples.R
import com.demo.kotlinexamples.activity.RecyclerViewActivity
import com.demo.kotlinexamples.activity.SplashActivity
import com.demo.kotlinexamples.model.Model
import kotlinx.android.synthetic.main.row_list.view.*

class Adapter(val context: RecyclerViewActivity, val list: ArrayList<Model>): RecyclerView.Adapter<Adapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,SplashActivity::class.java))
            context.finish()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(model: Model) {
            itemView.txtTitle.text = model.title
            itemView.imgProfile.setImageResource(model.profile)
        }
    }
}
