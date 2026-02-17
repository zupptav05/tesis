package com.zenboom.app.ui.exercises

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zenboom.app.R
import java.util.Random
import kotlin.math.sin

class BubblesFragment : Fragment(R.layout.fragment_bubbles) {

    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = true
    private val random = Random()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val container = view.findViewById<FrameLayout>(R.id.container_bubbles)

        // Fondo azul cielo muy suave
        container.setBackgroundColor(0xFFE1F5FE.toInt())

        startBubbleGenerator(container)

        view.findViewById<Button>(R.id.btn_back_bubbles).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun startBubbleGenerator(container: ViewGroup) {
        val generator = object : Runnable {
            override fun run() {
                if (!isPlaying) return
                createRealisticBubble(container)
                // Burbujas aleatorias cada 0.3 a 0.8 segundos
                handler.postDelayed(this, random.nextInt(500).toLong() + 300)
            }
        }
        handler.post(generator)
    }

    private fun createRealisticBubble(container: ViewGroup) {
        val bubble = View(context)
        val size = random.nextInt(200) + 80 // Burbujas grandes

        // Asegúrate de tener realistic_bubble.xml en drawable
        bubble.setBackgroundResource(R.drawable.realistic_bubble)

        val startX = random.nextInt(container.width - size).toFloat()
        val params = FrameLayout.LayoutParams(size, size)
        params.topMargin = container.height
        bubble.layoutParams = params
        bubble.x = startX

        container.addView(bubble)

        // Animación de "baile" (subir + tambalear)
        val duration = 6000 + random.nextInt(4000).toLong()
        val endY = -(size + 100f)

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = duration
        animator.interpolator = LinearInterpolator()

        val wobbleFreq = random.nextFloat() * 3f + 2f
        val wobbleAmp = random.nextInt(150).toFloat() + 50f

        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            bubble.translationY = container.height - (container.height - endY) * progress
            bubble.translationX = startX + sin(progress * wobbleFreq * Math.PI * 2) * wobbleAmp
        }

        bubble.setOnClickListener {
            animator.cancel()
            // Efecto POP al explotar
            bubble.animate()
                .scaleX(2f).scaleY(2f).alpha(0f)
                .setDuration(150)
                .withEndAction { container.removeView(bubble) }
                .start()
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(bubble)
            }
        })
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPlaying = false
        handler.removeCallbacksAndMessages(null)
    }
}