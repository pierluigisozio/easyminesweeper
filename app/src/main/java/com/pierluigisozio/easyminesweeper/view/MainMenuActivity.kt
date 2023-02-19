package com.pierluigisozio.easyminesweeper.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.pierluigisozio.easyminesweeper.R

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val btnStart : Button = findViewById(R.id.btnPlayGame)
        val btnExit : Button = findViewById(R.id.btnExit)
        btnStart.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, GameActivity::class.java)
            startActivity(intent)
        }
        btnExit.setOnClickListener {
            finishAffinity()
        }
    }
}