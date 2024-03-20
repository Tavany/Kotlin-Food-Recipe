package com.sehatin.ittp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sehatin.ittp.DetailActivity
import com.sehatin.ittp.mydataku
import com.sehatin.ittp.databinding.ItemGridBinding

class GridMyDataAdapter(private val listMyData: ArrayList<mydataku>): RecyclerView.Adapter<GridMyDataAdapter.GridViewHolder> () {
    inner class GridViewHolder(private val binding: ItemGridBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(myData: mydataku) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(myData.photo)
                    .apply(RequestOptions().override(1350, 1550))
                    .into(imgItemPhoto)

                itemView.setOnClickListener {
                    val moveWithObjectIntent = Intent(itemView.context, DetailActivity::class.java)
                    moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MY_DATA, myData)
                    itemView.context.startActivity(moveWithObjectIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun getItemCount(): Int = listMyData.size

    override fun onBindViewHolder(holder: GridMyDataAdapter.GridViewHolder, position: Int) {
        holder.bind(listMyData[position])
    }

}