package com.bappeda.publication_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.response.DetailKecamatan
import com.bumptech.glide.Glide



class PembangunanAdapter(private val detailKecamatanList: List<DetailKecamatan>) :
    RecyclerView.Adapter<PembangunanAdapter.DetailKecamatanViewHolder>() {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailKecamatanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pembangunan, parent, false)
        return DetailKecamatanViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailKecamatanViewHolder, position: Int) {
        val detailKecamatan = detailKecamatanList[position]
        holder.bind(detailKecamatan)
    }

    override fun getItemCount(): Int {
        return detailKecamatanList.size
    }

    inner class DetailKecamatanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pembangunanNameTextView: TextView = itemView.findViewById(R.id.pembangunanNameEditText)
        private val pembangunanDescriptionTextView: TextView = itemView.findViewById(R.id.pembangunanDescriptionEditText)
        private val kecamatanTextView: TextView = itemView.findViewById(R.id.Kecamatan)
        private val gambarImageView: ImageView = itemView.findViewById(R.id.pembangunanImageView)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val detailKecamatan = detailKecamatanList[position]
                    listener?.onItemClick(detailKecamatan)
                }
            }
        }

        fun bind(detailKecamatan: DetailKecamatan) {
            // Bind data ke tampilan item RecyclerView
            pembangunanNameTextView.text = detailKecamatan.bidang
            kecamatanTextView.text = detailKecamatan.namaKecamatan
            pembangunanDescriptionTextView.text = detailKecamatan.judul

            Glide.with(itemView)
                .load("http://192.168.1.21:3002/kecamatan/" + detailKecamatan.gambar1)
                .placeholder(R.drawable.place_holder)
                .circleCrop()
                .into(gambarImageView)

        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(detailKecamatan: DetailKecamatan)
    }
}