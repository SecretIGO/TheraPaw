package com.experiments.therapaw.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.experiments.therapaw.databinding.ActivitySplashBinding
import com.experiments.therapaw.view.main.MainActivity
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timer().schedule(3000) {
            MainActivity.launch(this@SplashActivity)
            finish()
        }

        animateCard(binding.animCard1)
        animateCard(binding.animCard2)
        animateCard(binding.animCard3)
        animateLogo(binding.animLogo)

    }

    private fun animateLogo(card: CardView){
        val growAnimatorX = ObjectAnimator.ofFloat(card, "scaleX", 0.1f, 1.3f).apply {
            duration = 600
        }
        val growAnimatorY = ObjectAnimator.ofFloat(card, "scaleY", 0.1f, 1.3f).apply {
            duration = 600
        }

        val shrinkAnimatorX = ObjectAnimator.ofFloat(card, "scaleX", 1.3f, 1f).apply {
            duration = 300
        }
        val shrinkAnimatorY = ObjectAnimator.ofFloat(card, "scaleY", 1.3f, 1f).apply {
            duration = 300
        }

        val growAnimatorSet = AnimatorSet().apply {
            playTogether(growAnimatorX, growAnimatorY)
            duration = 600
        }

        val shrinkAnimatorSet = AnimatorSet().apply {
            playTogether(shrinkAnimatorX, shrinkAnimatorY)
            duration = 350
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(growAnimatorSet, shrinkAnimatorSet)

        animatorSet.start()
    }

    private fun animateCard(card: View) {
        val shrinkAnimatorX = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0.86f).apply {
            duration = 300
        }
        val shrinkAnimatorY = ObjectAnimator.ofFloat(card, "scaleY", 1f, 0.86f).apply {
            duration = 300
        }
        val growAnimatorX = ObjectAnimator.ofFloat(card, "scaleX", 0.85f, 1f).apply {
            duration = 300
        }
        val growAnimatorY = ObjectAnimator.ofFloat(card, "scaleY", 0.85f, 1f).apply {
            duration = 300
        }

        val rotateAnimator = ObjectAnimator.ofFloat(card, "rotation", 0f, 100f).apply {
            duration = 1000
        }

        val growAnimatorSet = AnimatorSet().apply {
            playTogether(growAnimatorX, growAnimatorY, rotateAnimator)
            duration = 300
        }

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(shrinkAnimatorX, shrinkAnimatorY, growAnimatorSet)

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                animatorSet.start()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        animatorSet.start()
    }
}