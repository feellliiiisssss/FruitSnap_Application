package com.dicoding.mybuah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class BuahAdapterAdapter(private val listTips: ArrayList<Tips>) : RecyclerView.Adapter<BuahAdapterAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_buah_adapter, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listTips.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (judul, description, gambar, tanggal) = listTips[position]
        holder.imgPhoto.setImageResource(gambar)
        holder.tvName.text = judul
        holder.tvDescription.text = description
        holder.tvTanggal.text = tanggal
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listTips[holder.adapterPosition]) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Tips)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        val tvTanggal : TextView = itemView.findViewById(R.id.tv_tanggal)
    }

}