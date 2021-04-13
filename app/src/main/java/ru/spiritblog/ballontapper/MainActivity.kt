package ru.spiritblog.ballontapper

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {


    lateinit var contentView: ViewGroup

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawableResource(R.mipmap.background)
        setContentView(R.layout.activity_main)

        contentView = findViewById<ViewGroup>(R.id.content_view)

        contentView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setToFullScreen()
            }
            return@setOnTouchListener false
        }


    }


    override fun onResume() {
        super.onResume()
        setToFullScreen()
    }


    private fun setToFullScreen() {

        contentView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )


    }


}

