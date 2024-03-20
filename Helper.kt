package com.sehatin.ittp

import android.database.Cursor
import com.sehatin.ittp.data.Quote
import com.sehatin.ittp.db.DatabaseContract
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Helper {
    const val EXTRA_QUOTE = "extra_quote"
    const val EXTRA_POSITION = "extra_position"
    const val RESULT_ADD = 101
    const val RESULT_UPDATE = 201
    const val RESULT_DELETE = 301
    const val ALERT_DIALOG_CLOSE = 10
    const val ALERT_DIALOG_DELETE = 20
}