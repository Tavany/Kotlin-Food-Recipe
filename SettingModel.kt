package com.sehatin.ittp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingModel (
    var name: String? = null,
    var nim: Int = 0,
    var gender: Boolean = false,
    var email: String? = null,
//    var password: String? = null
    var age: Int = 0,
    var phoneNumber: String? = null,
    var isDarkTheme: Boolean = false
): Parcelable