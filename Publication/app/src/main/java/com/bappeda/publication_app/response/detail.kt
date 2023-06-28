package com.bappeda.publication_app.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Detail(
    @SerializedName("id") val id: Int,
    @SerializedName("id_kecamatan") val idKecamatan: Int,
    @SerializedName("nama_kecamatan") val namaKecamatan: String,
    @SerializedName("bidang") val bidang: String,
    @SerializedName("judul") val judul: String,
    @SerializedName("anggaran") val anggaran: String,
    @SerializedName("lokasi") val lokasi: String,
    @SerializedName("koordinat") val koordinat: String,
    @SerializedName("tahun") val tahun: String,
    @SerializedName("gambar1") val gambar1: String,
    @SerializedName("gambar2") val gambar2: String ?,
    @SerializedName("gambar3") val gambar3: String ?
) : Parcelable


