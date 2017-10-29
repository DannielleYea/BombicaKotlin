package com.example.zvjerka.bombica

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.zvjerka.bombica_kotlin.R.layout.activity_game
import kotlinx.android.synthetic.main.activity_game.*


import java.util.concurrent.TimeUnit


class game : AppCompatActivity(), View.OnClickListener {


    internal var tipke = arrayOfNulls<Button>(10)
    internal var znamenke = arrayOfNulls<TextView>(10)
    internal var sifra = IntArray(7)
    internal lateinit var dretve: Array<Thread?>
    internal lateinit var D_vib: vibra
    internal var CDT: CountDownTimer? = null
    internal var do_kraja: Long = 15000
    internal var zadnji: Long = 0
    internal lateinit var vib: Vibrator
    internal var pogodeni = -1
    internal var vibi = 100

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_game)


        vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        dretve = arrayOfNulls(7)
        for (i in 0..6)
            dretve[i] = pozicija(i)

        tipke = arrayOf(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)
        znamenke = arrayOf(X0, X1, X2, X3, X4, X5, X6)

        for (i in 0..9)
            tipke[i]?.setOnClickListener(this)
        //end = Intent(this, end_screen::class.java)
        rand()
    }

    override fun onBackPressed() {
        for (i in 0..6)
            dretve[i]?.interrupt()

        D_vib.interrupt()

        finish()
    }

    override fun onStart() {
        super.onStart()

        for (i in 0..6)
            dretve[i]?.start()

    }

    override fun onResume() {
        super.onResume()
        D_vib = vibra()

        CDT = null

        CDT = object : CountDownTimer(do_kraja, 10) {
            internal var radi = true

            override fun onTick(mili: Long) {
                textView11.text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toSeconds(mili), (mili - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(mili))) / 10)
                if (mili < 10000 && radi) {
                    D_vib.start()
                    radi = false
                }
                do_kraja = mili
            }

            override fun onFinish() {
                for (y in 0..6)
                    dretve[y]?.interrupt()
                D_vib.interrupt()
                for (i in 0..9)
                    tipke[i]?.setClickable(false)
                //end.putExtra("VRIJEME", 0)
                //startActivity(end)
            }
        }.start()
    }


    override fun onClick(p0: View?){

        if (System.currentTimeMillis() - zadnji < 350)
            return
        zadnji = System.currentTimeMillis()

        when (p0?.id) {
            T0.id -> {
                obrada(0)
            }

            T1.id -> {
                obrada(1)
            }

            T2.id -> {
                obrada(2)
            }

            T3.id -> {
                obrada(3)
            }

            T4.id -> {
                obrada(4)

            }

            T5.id -> {
                obrada(5)
            }

            T6.id -> {
                obrada(6)
            }

            T7.id -> {
                obrada(7)
            }

            T8.id -> {
                obrada(8)
            }

            T9.id -> {
                 obrada(9)
            }
        }
    }

    private fun rand() {
        for (i in 0..6)
            sifra[i] = (Math.random() * 10).toInt()
    }

    private fun obrada(x: Int) {

        if (pogodeni == -1) pogodeni++


        if (x == sifra[pogodeni]) {

            znamenke[pogodeni]?.setText("" + x)
            znamenke[pogodeni]?.setTextColor(Color.parseColor("#009900"))
            pogodeni++

        } else {
            znamenke[pogodeni]?.setText("" + x)
            znamenke[pogodeni]?.setTextColor(Color.parseColor("#FF0000"))
        }

        if (pogodeni == 7) {
            CDT!!.cancel()

            for (i in 0..9)
                tipke[i]?.setClickable(false)
            // fuckcija koja hendla pobjedu

            //end.putExtra("VRIJEME", do_kraja)
            //startActivity(end)
            return

        }
    }

    internal inner class vibra : Thread() {

        @SuppressLint("MissingPermission")
        override fun run() {
            while (!Thread.interrupted()) {
                while (do_kraja > 350) {
                    vib.vibrate(vibi.toLong())

                    try {
                        Thread.sleep(1000)
                        vibi += 33
                    } catch (e: InterruptedException) {
                        throw RuntimeException("Thread interrupted..." + e)
                    }

                }
            }
            Thread.currentThread().interrupt()
        }
    }

    internal inner class pozicija(private val poz: Int) : Thread() {

        @Synchronized override fun run() {
            while (!Thread.interrupted()) {
                while (pogodeni < poz) {
                    runOnUiThread { znamenke[poz]?.setText("" + (Math.random() * 10).toInt()) }
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        throw RuntimeException("Thread interrupted..." + e)
                    }

                }
            }
            Thread.currentThread().interrupt()
        }
    }
}
