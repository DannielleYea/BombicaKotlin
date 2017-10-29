package com.example.zvjerka.bombica_kotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.zvjerka.bombica.game
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart(){
        super.onStart()
        val int = Intent(this.baseContext, game::class.java)

        start_btn.setOnClickListener{
            startActivity(int)
        }
    }
}

