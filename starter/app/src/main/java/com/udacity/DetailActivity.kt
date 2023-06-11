package com.udacity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.udacity.databinding.ActivityDetailBinding


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

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.contentDetail.fileName.text = intent.getStringExtra(FILE_NAME_EXTRA) ?: ""
        motionLayout = findViewById(R.id.contraint_layout)
        motionLayout.setTransitionListener(motionLayoutListener)
        binding.okButton.setOnClickListener {
            if (motionLayout.progress == 0.0f)
                motionLayout.transitionToEnd()
        }
    }
}
