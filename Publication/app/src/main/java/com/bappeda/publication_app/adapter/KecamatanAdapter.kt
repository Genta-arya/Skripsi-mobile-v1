import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bappeda.publication_app.R
import com.bappeda.publication_app.response.Kecamatan
import com.bumptech.glide.Glide



class KecamatanAdapter(
    private var originalData: List<Kecamatan>?,
    private val onItemClick: (idKecamatan: String) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var filteredData: List<Kecamatan>? = originalData
    private var isNoDataVisible = false

    companion object {
        private const val VIEW_TYPE_DATA = 0
        private const val VIEW_TYPE_NO_DATA = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kecamatan, parent, false)
                DataItemViewHolder(view)
            }
            VIEW_TYPE_NO_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_no_data, parent, false)
                NoDataViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DataItemViewHolder) {
            val item = filteredData?.get(position)
            item?.let {
                holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isNoDataVisible) {
            1 // Jika data tidak ditemukan, hanya satu item_no_data yang ditampilkan
        } else {
            filteredData?.size ?: 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isNoDataVisible) {
            VIEW_TYPE_NO_DATA
        } else {
            VIEW_TYPE_DATA
        }
    }

    fun setData(data: List<Kecamatan>?) {
        originalData = data
        filteredData = data
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = GridLayoutManager(recyclerView.context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) == VIEW_TYPE_NO_DATA) {
                    2 // Jika data tidak ditemukan, gunakan spanCount 2 agar item_no_data ditampilkan di tengah
                } else {
                    1
                }
            }
        }
        recyclerView.layoutManager = layoutManager
    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT
        holder.itemView.layoutParams = layoutParams
    }

    inner class DataItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kecamatanNameTextView: TextView = itemView.findViewById(R.id.kecamatanNameEditText)
        private val kecamatanIdTextView: TextView = itemView.findViewById(R.id.kecamatanIdEditText)
        private val kecamatanImageView: ImageView = itemView.findViewById(R.id.kecamatanImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = filteredData?.get(position)
                    item?.let {
                        onItemClick(it.id_kecamatan.toString())
                    }
                }
            }
        }

        fun bind(kecamatan: Kecamatan) {
            kecamatanNameTextView.text = kecamatan.nama_kecamatan
            kecamatanIdTextView.text = kecamatan.id_kecamatan.toString()

            Glide.with(itemView)
                .load("http://192.168.1.21:3002/kecamatan/" + kecamatan.gambar)
                .circleCrop()
                .into(kecamatanImageView)
        }
    }

    inner class NoDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noDataTextView: TextView = itemView.findViewById(R.id.noDataTextView)

        init {
            noDataTextView.text = "Kecamatan tidak ditemukan"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val query = constraint?.toString()?.trim()

                if (query.isNullOrEmpty()) {
                    filterResults.values = originalData
                    isNoDataVisible = false
                } else {
                    val filteredList = originalData?.filter { kecamatan ->
                        kecamatan.nama_kecamatan.contains(query, ignoreCase = true)
                    }
                    if (filteredList.isNullOrEmpty()) {
                        isNoDataVisible = true
                    } else {
                        isNoDataVisible = false
                    }
                    filterResults.values = filteredList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as? List<Kecamatan>
                notifyDataSetChanged()
            }
        }
    }
}
