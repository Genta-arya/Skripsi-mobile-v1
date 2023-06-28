package com.bappeda.publication_app.response


data class KecamatanResponse(
	val data: List<Kecamatan>,
	val message: String
)
data class Kecamatan(
	val id: Int,
	val nama_kecamatan: String,
	val id_kecamatan: Int,
	val deskripsi: String,
	val gambar: String
)