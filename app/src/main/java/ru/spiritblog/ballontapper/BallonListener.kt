package ru.spiritblog.ballontapper

import android.animation.Animator
import android.animation.ValueAnimator

class BalloonListener(val balloon: Balloon) : Animator.AnimatorListener,
    ValueAnimator.AnimatorUpdateListener {


    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (!balloon.isPopped()) {
            balloon.y = animation?.animatedValue as Float
        }
    }


    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {
        if (!balloon.isPopped()) {
            balloon.pop(false)
        }

    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationRepeat(animation: Animator?) {

    }
}