package ru.spiritblog.ballontapper

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var contentView: ViewGroup
    var colors: Array<Int> = arrayOf(0, 0, 0)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        colors[0] = Color.argb(255, 255, 0, 0)
        colors[1] = Color.argb(255, 0, 255, 0)
        colors[2] = Color.argb(255, 0, 0, 255)


        window.setBackgroundDrawableResource(R.mipmap.background)
        setContentView(R.layout.activity_main)

        contentView = findViewById<ViewGroup>(R.id.content_view)

        contentView.setOnTouchListener { v, event ->


            var curColor = colors[nextColor()]
            val btemp: Balloon = Balloon(
                this, curColor, 100, 1
            )
            btemp.y = event.y
            btemp.x = event.x
            contentView.addView(btemp)

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


    companion object {

        fun nextColor(): Int {

            val max = 2
            val min = 0
            var retval = 0

            val random = Random()
            retval = random.nextInt((max - min) + 1) + min

            return retval


        }


    }


}

