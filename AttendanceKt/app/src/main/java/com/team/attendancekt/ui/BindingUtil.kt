package com.team.attendancekt.ui

import android.net.Uri
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.*
import com.team.attendancekt.R
import com.team.attendancekt.model.entity.ChatMessage
import com.team.attendancekt.model.entity.MessageType

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

        @JvmStatic
        @BindingAdapter("path")
        fun setImageUri(imageView: ImageView, imageFilePath: String?) {
            if (imageFilePath != null && imageFilePath.isNotEmpty()) imageView.setImageURI(
                Uri.parse(
                    imageFilePath
                )
            )
        }

        @JvmStatic
        @BindingAdapter("android:checkedButton")
        fun setCheckedButton(radioGroup: RadioGroup, id: Int) {
            if (id != radioGroup.checkedRadioButtonId) radioGroup.check(id)
        }

        @JvmStatic
        @BindingAdapter("android:text")
        fun setMessage(textView: TextView, msg: ChatMessage?) {
            msg?.also {
                when (it.messageType) {
                    MessageType.JOIN -> textView.text =
                        textView.context.getString(R.string.chat_info_join, it.sender)
                    MessageType.LEAVE -> textView.text =
                        textView.context.getString(R.string.chat_info_leave, it.sender)
                    else -> {
                    }
                }
            }
        }
    }
}