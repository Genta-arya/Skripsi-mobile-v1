package com.example.githubuser.Room


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class FvUser(
    @SerializedName("id_kecamatan") val idKecamatan: Int,
    @SerializedName("nama_kecamatan") val namaKecamatan: Int,
    @SerializedName("pembangunan") val pembangunan: String,
    @SerializedName("deskripsi") val deskripsi: String,
    @SerializedName("gambar1") val gambar1: String,
    @SerializedName("gambar2") val gambar2: String ?,
    @SerializedName("gambar3") val gambar3: String ?,
    @PrimaryKey
    @SerializedName("id") val id: String,

    ) : Serializable
