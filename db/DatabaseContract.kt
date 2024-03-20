package com.sehatin.ittp.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class QuoteColumns : BaseColumns {
        companion object {
            const val TABLE_QUOTE = "quote"
            const val _ID = "_id"
            const val TITLE = "title"
            const val AUTHOR = "author"
            const val DESCRIPTION = "description"
            const val CATEGORY = "category"
            const val DATE = "date"
            const val PUBLISH = "publish"}
    }
}