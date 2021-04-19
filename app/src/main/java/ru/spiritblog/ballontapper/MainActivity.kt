package ru.spiritblog.ballontapper

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity(), PopListener {

    lateinit var contentView: ViewGroup
    lateinit var levelDisplay: TextView
    lateinit var scoreDisplay: TextView
    lateinit var btn: Button
    private var colors: Array<Int> = arrayOf(0, 0, 0)
    private val pinImages: ArrayList<ImageView> = ArrayList()
    private val balloons: ArrayList<Balloon> = ArrayList()
    private var scrWidth = 0F
    private var scrHeight = 0F
    private var level = 0
    private var userScore = 0
    private val numberOfPins = 5
    private var pinsUsed = 0
    private var isGameStopped = true
    private var balloonsLaunched = 0
    private var balloonsPerLevel = 8
    private var balloonsPopped = 0

    private lateinit var audio: Audio


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        audio = Audio(this)
        audio.prepareMediaPlayer(this)



        colors[0] = Color.argb(255, 255, 0, 0)
        colors[1] = Color.argb(255, 0, 255, 0)
        colors[2] = Color.argb(255, 0, 0, 255)


        window.setBackgroundDrawableResource(R.mipmap.background)
        setContentView(R.layout.activity_main)

        contentView = findViewById<ViewGroup>(R.id.content_view)
        levelDisplay = findViewById(R.id.level_display)
        scoreDisplay = findViewById(R.id.score_display)


        btn = findViewById(R.id.go_button)
        btn.setOnClickListener {
            startLevel()
        }

        pinImages.add(findViewById<ImageView>(R.id.pushpin1))
        pinImages.add(findViewById<ImageView>(R.id.pushpin2))
        pinImages.add(findViewById<ImageView>(R.id.pushpin3))
        pinImages.add(findViewById<ImageView>(R.id.pushpin4))
        pinImages.add(findViewById<ImageView>(R.id.pushpin5))


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

        val viewTreeObserver = contentView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    scrWidth = contentView.width.toFloat()
                    scrHeight = contentView.height.toFloat()
                    Log.d(TAG, "$scrHeight")

                }
            })
        }


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


    private fun startLevel() {

        if (isGameStopped) {
            isGameStopped = false
            startGame()
        }

        updateGameStats()
        LevelLoop(level).start()

    }

    private fun startGame() {

        audio.playMusic()

        //reset the scores
        userScore = 0
        level = 1
        updateGameStats()

        //reset the pushpin images
        pinImages.forEach {
            it.setImageResource(R.drawable.pin)
        }


    }


    fun launchBalloon(xPos: Int) {

        balloonsLaunched++

        val curColor = colors[nextColor()]
        val btemp: Balloon = Balloon(
            this, curColor, 100, 1
        )
        btemp.y = scrHeight
        btemp.x = xPos.toFloat()

        contentView.addView(btemp)
        balloons.add(btemp)
        btemp.release(scrHeight, 5000)
        Log.d(TAG, "Ballon created")


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


    inner class LevelLoop(argLevel: Int) : Thread() {


        private val shortDelay = 500
        private val longDelay = 1500
        private var maxDelay = 0
        private var minDelay = 0
        private var delay = 0
        private var loopLevel = argLevel

        private var balloonsLaunched = 0


        override fun run() {
            while (balloonsLaunched < balloonsPerLevel) {
                balloonsLaunched++
                var random = Random(Date().time)
                val xPosition: Int = random.nextInt(scrWidth.toInt() - 200)

                maxDelay = Math.max(shortDelay, (longDelay - (loopLevel - 1) * 500))
                minDelay = maxDelay / 2
                delay = random.nextInt(minDelay) + minDelay
                Log.i(TAG, String.format("Thread delay = %d", delay))


                try {
                    Thread.sleep(delay.toLong())
                } catch (e: InterruptedException) {
                    Log.e(TAG, e.message)

                }
                // need to wrap this on runUiThread

                runOnUiThread {
                    launchBalloon(xPosition)
                }


            }


        }


    }


    override fun popBalloon(bal: Balloon, isTouched: Boolean) {

        audio.playSound()


        balloonsPopped++
        contentView.removeView(bal)

        if (isTouched) {
            userScore++
            scoreDisplay.text = String.format("%d", userScore)

        } else {

            pinsUsed++
            if (pinsUsed <= pinImages.size) {
                pinImages[pinsUsed - 1].setImageResource(R.drawable.pin_broken)
                Toast.makeText(this, "Ouch!", Toast.LENGTH_SHORT)
                    .show()
            }
            if (pinsUsed == numberOfPins) {
                gameOver()
                return
            }


        }

        if (balloonsPopped == balloonsPerLevel) {
            finishLevel()
        }


    }


    private fun finishLevel() {
        Log.d(TAG, "FINISH LEVEL")

        val message = String.format("Level %d finished!", level)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        level++
        updateGameStats()
        btn.setText(String.format("Start level %d", level))

        Log.d(TAG, String.format("BalloonsLaunched = %d", balloonsLaunched))
        balloonsPopped = 0

    }


    private fun gameOver() {


        isGameStopped = true
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        btn.text = "Play game"


        balloons.forEach {
            it.setPopped(true)
            contentView.removeView(it)
        }


        balloons.clear()
        audio.pauseMusic()


    }


    private fun updateGameStats() {
        levelDisplay.text = String.format("%s", level)
        scoreDisplay.text = String.format("%s", userScore)


    }


}

