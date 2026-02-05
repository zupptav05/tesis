package com.zenboom.app.ui

import android.animation.*
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.zenboom.app.R

class ExercisesFragment : Fragment(R.layout.fragment_exercise) {
    private var isRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bubble = view.findViewById<View>(R.id.breathing_bubble)
        val btn = view.findViewById<Button>(R.id.btn_start_exercise)
        val txt = view.findViewById<TextView>(R.id.tv_instruction)

        btn.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                btn.text = "Detener"
                animateBreath(bubble, txt)
            } else {
                btn.text = "Iniciar"
                bubble.clearAnimation()
                txt.text = "¡Buen trabajo, Zen Master!"
            }
        }
    }

    private fun animateBreath(view: View, label: TextView) {
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 2.2f).setDuration(4000)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2.2f).setDuration(4000)
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 2.2f, 1f).setDuration(4000)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 2.2f, 1f).setDuration(4000)

        val set = AnimatorSet()
        set.play(scaleUpX).with(scaleUpY)
        set.play(scaleDownX).with(scaleDownY).after(scaleUpX)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) { label.text = "Inhala..." }
            override fun onAnimationRepeat(animation: Animator) { /* Opcional */ }
            override fun onAnimationEnd(animation: Animator) {
                if (isRunning) {
                    label.text = "Exhala..."
                    set.start()
                }
            }
        })
        set.start()
    }
}