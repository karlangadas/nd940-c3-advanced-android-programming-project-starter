package com.udacity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME_EXTRA = "FILE_NAME"
    }

    private lateinit var motionLayout: MotionLayout
    private val motionLayoutListener = object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
            onBackPressed()
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        findViewById<TextView>(R.id.file_name).text = intent.getStringExtra(FILE_NAME_EXTRA) ?: ""
        motionLayout = findViewById(R.id.contraint_layout)
        motionLayout.setTransitionListener(motionLayoutListener)
        findViewById<TextView>(R.id.ok_button)?.setOnClickListener {
            if (motionLayout.progress == 0.0f)
                motionLayout.transitionToEnd()
        }
    }

}
