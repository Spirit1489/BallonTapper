package ru.spiritblog.ballontapper

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView

class Balloon(context: Context) : AppCompatImageView(context) {


    public constructor(
        context: Context, color: Int,
        height: Int, level: Int
    ) : this(context) {

        setImageResource(R.mipmap.balloon)
        setColorFilter(color.toInt())
        val width = height / 2
        val dpHeight = pixelsToDp(height, context)
        val dpWidth = pixelsToDp(width, context)

        val params: ViewGroup.LayoutParams =
            ViewGroup.LayoutParams(dpWidth, dpHeight)

        layoutParams = params

    }


    companion object {
        fun pixelsToDp(px: Int, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }


}