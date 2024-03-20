package com.sehatin.ittp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sehatin.ittp.DetailActivity
import com.sehatin.ittp.MyData
import com.sehatin.ittp.databinding.ItemCardviewBinding

class CardViewMyDataAdapter(private val listMyData: ArrayList<MyData>) :
    RecyclerView.Adapter<CardViewMyDataAdapter.CardViewViewHolder>() {

    inner class CardViewViewHolder(private val binding: ItemCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myData: MyData){
            with(binding){
                Glide.with(itemView.context)
                    .load(myData.photo)
                    .apply(RequestOptions().override(350, 550))
                    .into(imgItemPhoto)
                tvItemName.text= myData.name
                tvItemDetail.text = myData.description
//                btnSetFavorite.setOnClickListener {
//                    Toast.makeText(itemView.context, "Favorite "+ (myData.name), Toast.LENGTH_SHORT).show() }
//                btnSetShare.setOnClickListener {
//                    Toast.makeText(itemView.context, "Share " + (myData.name), Toast.LENGTH_SHORT).show() }
                itemView.setOnClickListener {
                    val moveWithObjectIntent = Intent(itemView.context, DetailActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MY_DATA, myData)
                    itemView.context.startActivity(moveWithObjectIntent)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val binding =
            ItemCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewViewHolder(binding)
    }

    override fun getItemCount(): Int = listMyData.size

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.bind(listMyData[position])
    }
}