package com.sehatin.ittp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sehatin.ittp.*
import com.sehatin.ittp.databinding.ItemCardviewBinding
import com.sehatin.ittp.databinding.ItemListBinding

class ListMyDataAdapter (private val listMyData: ArrayList<mydataku>) :
    RecyclerView.Adapter<ListMyDataAdapter.ListViewHolder>() {

    inner class ListViewHolder (private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myData: mydataku) {
            with(binding){
                Glide.with(itemView.context)
                    .load(myData.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(ImgItemPhoto1)
                TvItemName1.text = myData.name
                TvItemDescription1.text = myData.description
                itemView.setOnClickListener {
                    val moveWithObjectIntent = Intent(itemView.context, DetailsActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailsActivity.EXTRA_MY_DATA, myData)
                    itemView.context.startActivity(moveWithObjectIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listMyData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMyData[position])
    }
}