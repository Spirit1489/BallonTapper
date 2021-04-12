package ru.spiritblog.ballontapper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        window.setBackgroundDrawableResource(R.mipmap.background)



        setContentView(R.layout.activity_main)
    }
}