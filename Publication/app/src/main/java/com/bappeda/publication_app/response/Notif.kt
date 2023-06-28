package com.bappeda.publication_app.response

import com.google.gson.annotations.SerializedName


data class NotifikasiResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    val data: List<Notifikasi>
)
data class Notifikasi(
    val id: Int,
    val id_kecamatan: Int,
    val nama_kecamatan: String,
    val pesan: String,
    val gambar: String
)
