package com.bappeda.publication_app.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.response.Detail
import android.widget.TextView
import com.bumptech.glide.Glide
import android.content.Context
import android.content.Intent
import com.bappeda.publication_app.ui.home.ui.ui.bookmark.DetailBookmarkActivity
class BookmarkAdapter(private val favoritePagesList: List<Detail>) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null
    private  var API = "192.168.150.59:3001"

    interface OnItemClickListener {
        fun onItemClick(detail: Detail)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val kecamatan: TextView = itemView.findViewById(R.id.Kecamatan)
        val pembangunan: TextView = itemView.findViewById(R.id.pembangunanNameEditText)
        val deskripsi: TextView = itemView.findViewById(R.id.pembangunanDescriptionEditText)
        val image: ImageView = itemView.findViewById(R.id.pembangunanImageView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(detail: Detail) {
            kecamatan.text = detail.namaKecamatan
            pembangunan.text = detail.bidang
            deskripsi.text = detail.judul
            Glide.with(itemView.context)
                .load("http://192.168.1.21:3002/kecamatan/" + detail.gambar1)
                .placeholder(R.drawable.place_holder)
                .circleCrop()
                .into(image)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(favoritePagesList[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pembangunan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoritePagesList[position])
    }

    override fun getItemCount(): Int {
        return favoritePagesList.size
    }
}

