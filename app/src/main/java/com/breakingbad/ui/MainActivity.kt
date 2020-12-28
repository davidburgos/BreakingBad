package com.breakingbad.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.breakingbad.R
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }
}
