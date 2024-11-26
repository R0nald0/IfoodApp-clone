package com.example.domain.models

import android.net.Uri

data class UploadStorage(
    val nome :String,
    val uriImage: Uri,
    val local: String,
) {
}