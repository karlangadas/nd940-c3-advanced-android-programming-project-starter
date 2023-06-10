package com.udacity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME_EXTRA = "FILE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        findViewById<TextView>(R.id.file_name).text = intent.getStringExtra(FILE_NAME_EXTRA) ?: ""
        findViewById<TextView>(R.id.ok_button)?.setOnClickListener {
            onBackPressed()
        }
    }

}
