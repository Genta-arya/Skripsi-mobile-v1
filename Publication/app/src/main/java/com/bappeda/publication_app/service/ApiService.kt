package com.bappeda.publication_app.service
import com.bappeda.publication_app.response.*

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("kecamatan/")
    fun getKecamatan(): Call<KecamatanResponse>

    @GET("detail/{id_kecamatan}")
    fun getListPembangunan(@Path("id_kecamatan") idKecamatan: String): Call<DetailKecamatanResponse>

    @GET("halaman/{id}")
    fun getDetailPembangunan(@Path("id") id: String): Call<Detail>


    @GET("api/notifikasi")
    fun getNotifikasi(): Call<NotifikasiResponse>

    @DELETE("api/notifikasi")
    fun deleteNotifikasi(): Call<NotifikasiResponse>




}