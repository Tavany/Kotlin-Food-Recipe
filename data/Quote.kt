package com.sehatin.ittp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    var id: String? = null,
    var title: String? = null,
    var descriptions: String? = null,
    var category: String? = null,
    var date: com.google.firebase.Timestamp? = null
) : Parcelable