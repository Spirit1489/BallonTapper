package ru.spiritblog.ballontapper


import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView


private const val TAG = "Balloon"


class Balloon(context: Context) : AppCompatImageView(context), View.OnTouchListener {


    private var listener: BalloonListener = BalloonListener(this)
    private lateinit var animator: ValueAnimator
    private var isPopped = false
    private var mainActivity: PopListener = context as PopListener


    public constructor(
        context: Context, color: Int,
        height: Int, level: Int
    ) : this(context) {


        setOnTouchListener(this)


        setImageResource(R.mipmap.balloon)
        setColorFilter(color.toInt())
        val width = height / 2
        val dpHeight = pixelsToDp(height, context)
        val dpWidth = pixelsToDp(width, context)

        val params: ViewGroup.LayoutParams =
            ViewGroup.LayoutParams(dpWidth, dpHeight)

        layoutParams = params

    }


    fun release(scrHeight: Float, duration: Int) {

        animator = ValueAnimator()
        animator.setDuration(duration.toLong())
        animator.setFloatValues(scrHeight, 0f)
        animator.interpolator = LinearInterpolator()
        animator.setTarget(this)
        animator.addListener(listener)
        animator.addUpdateListener(listener)
        animator.start()


    }


    companion object {
        fun pixelsToDp(px: Int, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, px.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        Log.d(TAG, "TOUCHED")
        if (!isPopped) {
            mainActivity.popBalloon(this, true)
            isPopped = true
            animator.cancel()

        }
        return true
    }

    fun pop(isTouched: Boolean) {
        mainActivity.popBalloon(this, isTouched)
    }

    fun isPopped():Boolean{
        return isPopped
    }

    fun setPopped(boolean: Boolean){
        isPopped = boolean
    }


}