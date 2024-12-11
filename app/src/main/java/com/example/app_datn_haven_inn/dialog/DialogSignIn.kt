package com.example.app_datn_haven_inn.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextView
import com.example.app_datn_haven_inn.R
import com.example.app_datn_haven_inn.ui.auth.SignIn

class DialogSignIn(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
    private var onPressListener: OnPressListener? = null
    private val tvCancel: TextView
    private val tvSubmit: TextView

    init {
        setContentView(R.layout.dialog_sigin)
        hideNavigationBar()
        val attributes = window!!.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = attributes
        window!!.setSoftInputMode(16)

        tvCancel = findViewById(R.id.tv_cancel)
        tvSubmit = findViewById(R.id.tv_submit)

        onClick()
    }

    fun init(onPress: OnPressListener?) {
        this.onPressListener = onPress
    }

    private fun onClick() {
        tvCancel.setOnClickListener  {
            dismiss()
        }

        tvSubmit.setOnClickListener {
            val intent = Intent(context, SignIn::class.java)
            context.startActivity(intent)
        }

    }

    fun hideNavigationBar() {

        val decorView = window?.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 (API level 30) and above
            decorView?.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Below Android 11
            decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            // Listener để ẩn lại thanh điều hướng khi người dùng tương tác
            decorView?.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    Handler().postDelayed({
                        decorView.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                )
                    }, 3000)
                }
            }
        }
    }

    interface OnPressListener {
        fun cancel()
        fun submit()
    }
}