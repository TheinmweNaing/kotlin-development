package com.team.attendancekt.ui

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

class BindingUtil {

    companion object {

        @JvmStatic
        @BindingAdapter("android:text")
        fun setNumber(editText: EditText, value: Int) {
            editText.setText(value.toString())
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun getNumber(editText: EditText): Int {
            val value = editText.text.toString()
            return if (value.isNotEmpty()) value.toInt() else 0
        }
    }
}