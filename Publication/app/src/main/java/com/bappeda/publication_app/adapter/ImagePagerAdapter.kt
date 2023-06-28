package com.bappeda.publication_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bappeda.publication_app.R
import com.bappeda.publication_app.response.Detail
import com.bumptech.glide.Glide
import retrofit2.Response.error

class ImagePagerAdapter(private val detailData: Detail) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = getImageUrl(position)

        if (imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(imageUrl)

                .placeholder(R.drawable.place_holder)
                .into(holder.imageView)
        } else {
            // Jika URL gambar tidak tersedia, Anda dapat menampilkan gambar placeholder atau melakukan tindakan lain
            holder.imageView.setImageResource(R.drawable.place_holder)
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        if (detailData.gambar1.isNotEmpty()) {
            count++
        }
        if (detailData.gambar2 != null && detailData.gambar2.isNotEmpty()) {
            count++
        }
        if (detailData.gambar3 != null && detailData.gambar3.isNotEmpty()) {
            count++
        }
        return count
    }

    private fun getImageUrl(position: Int): String {
        return when (position) {
            0 -> "http://192.168.129.59:3001/halaman/${detailData.id}/${detailData.gambar1}"
            1 -> detailData.gambar2?.let { "http://192.168.1.21:3002/halaman/${detailData.id}/$it" } ?: ""
            2 -> detailData.gambar3?.let { "http://192.168.1.21:3002/halaman/${detailData.id}/$it" } ?: ""
            else -> ""
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}
