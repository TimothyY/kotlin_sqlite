package com.example.kotlin_sqlite

import android.provider.BaseColumns

object MsUserContract {
    // Table contents are grouped together in an anonymous object.
    object MsUser : BaseColumns {
        const val TABLE_MS_USER = "MsUser"
        const val MS_USER_EMAIL = "email"
        const val MS_USER_PASSWORD = "password"
    }
}
