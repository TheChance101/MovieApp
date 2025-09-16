package com.karrar.movieapp.utilities.view

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import com.karrar.movieapp.R

class MovieEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val editText: EditText
    private val helperText: TextView
    private val passwordEye: ImageView

    private var isEyeShow = true

    init {
        inflate(context, R.layout.view_edit_text, this)
        editText = findViewById(R.id.innerEditText)
        helperText = findViewById(R.id.helperText)
        passwordEye = findViewById(R.id.passwordEye)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MovieEditText)
        val helperTextValue = typedArray.getString(R.styleable.MovieEditText_helperText)
        val showEye = typedArray.getBoolean(R.styleable.MovieEditText_showEye, false)
        helperText.text = helperTextValue
        passwordEye.visibility = if (showEye) VISIBLE else GONE

        typedArray.recycle()

        passwordEye.setOnClickListener {
            val selection = editText.selectionStart
            isEyeShow = !isEyeShow
            passwordEye.setImageDrawable(
                if (isEyeShow)
                    ContextCompat.getDrawable(getContext(), R.drawable.eye_opened)
                else
                    ContextCompat.getDrawable(getContext(), R.drawable.eye_closed)
            )

            if (isEyeShow)
                editText.transformationMethod = null
            else
                editText.transformationMethod = PasswordTransformationMethod()

            editText.setSelection(selection)
        }
    }

    fun getText(): String = editText.text.toString()

    fun setText(value: String) {
        editText.setText(value)
    }

    fun setHelperText(value: String?) {
        helperText.text = value

        if (value != null && TextUtils.isEmpty(value.trim())) {
            helperText.visibility = GONE
        } else {
            helperText.visibility = VISIBLE
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("afterTextChanged")
        fun setAfterTextChanged(view: MovieEditText, listener: AfterTextChangedListener?) {
            val old = ListenerUtil.trackListener(
                view, listener, R.id.textWatcher
            ) as? TextWatcher
            if (old != null) view.editText.removeTextChangedListener(old)

            if (listener == null) return

            val watcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    listener.afterTextChanged(s)
                }
            }

            view.editText.addTextChangedListener(watcher)
            ListenerUtil.trackListener(view, watcher, R.id.textWatcher)
        }
    }

    fun interface AfterTextChangedListener {
        fun afterTextChanged(s: Editable?)
    }

    fun getEditText(): EditText = editText
}