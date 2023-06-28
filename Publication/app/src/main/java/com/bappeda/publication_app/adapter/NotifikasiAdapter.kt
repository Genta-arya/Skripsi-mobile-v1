package com.bappeda.publication_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.response.Notifikasi
import com.bumptech.glide.Glide


class NotifikasiAdapter : RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    private var notifikasiList: List<Notifikasi> = emptyList()

    fun setNotifikasiList(notifikasiList: List<Notifikasi>) {
        this.notifikasiList = notifikasiList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi, parent, false)
        return NotifikasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        val notifikasi = notifikasiList[position]
        holder.bind(notifikasi)
    }

    override fun getItemCount(): Int {
        return notifikasiList.size
    }

    inner class NotifikasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.pembangunanImageView)
        private val textkecamatan: TextView = itemView.findViewById(R.id.Kecamatan)
        private val textpesan: TextView = itemView.findViewById(R.id.pembangunanDescriptionEditText)

        fun bind(notifikasi: Notifikasi) {
            textkecamatan.text = notifikasi.nama_kecamatan
            textpesan.text = notifikasi.pesan

            Glide.with(itemView)
                .load("http://192.168.1.21:3002/api/notifikasi/" + notifikasi.gambar)
                .placeholder(R.drawable.place_holder)
                .circleCrop()
                .into(imageView)
        }
    }
}
