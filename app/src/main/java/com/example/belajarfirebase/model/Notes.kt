package com.example.belajarfirebase.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notes (
    var noteId: String,
    var judul: String,
    var konten: String,
    var uid: String,
    var imageUrl: String = ""
        ) : Parcelable