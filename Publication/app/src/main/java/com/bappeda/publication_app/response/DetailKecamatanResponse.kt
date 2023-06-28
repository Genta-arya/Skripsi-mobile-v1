package com.bappeda.publication_app.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
data class DetailKecamatanResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    val data: List<DetailKecamatan>
)

data class DetailKecamatan(

    @SerializedName("id")
    val id: Int,
    @SerializedName("id_kecamatan")
    val idKecamatan: Int,
    @SerializedName("nama_kecamatan")
    val namaKecamatan: String?,
    @SerializedName("bidang")
    val bidang: String,
    @SerializedName("judul")
    val judul: String,
    @SerializedName("anggaran")
    val anggaran: String,
    @SerializedName("lokasi")
    val lokasi: String,
    @SerializedName("koordinat")
    val koordinat: String,
    @SerializedName("tahun")
    val tahun: String,
    @SerializedName("gambar1")
    val gambar1: String,
    @SerializedName("gambar2")
    val gambar2: String,
    @SerializedName("gambar3")
    val gambar3: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(idKecamatan)
        parcel.writeString(namaKecamatan)
        parcel.writeString(bidang)
        parcel.writeString(judul)
        parcel.writeString(anggaran)
        parcel.writeString(lokasi)
        parcel.writeString(koordinat)
        parcel.writeString(tahun)
        parcel.writeString(gambar1)
        parcel.writeString(gambar2)
        parcel.writeString(gambar3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailKecamatan> {
        override fun createFromParcel(parcel: Parcel): DetailKecamatan {
            return DetailKecamatan(parcel)
        }

        override fun newArray(size: Int): Array<DetailKecamatan?> {
            return arrayOfNulls(size)
        }
    }
}
