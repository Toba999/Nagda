package com.dev.nagda.utils

import android.content.Context
import android.content.SharedPreferences
import com.dev.nagda.utils.FireStoreConstant.PREFS_NAME
import javax.inject.Inject


class SharedPrefManager @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


}
