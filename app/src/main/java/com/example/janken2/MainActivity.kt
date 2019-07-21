package com.example.janken2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gu.setOnClickListener{ onJankenButtonTapped(it) }
        choki.setOnClickListener{ onJankenButtonTapped(it) }
        pa.setOnClickListener{ onJankenButtonTapped(it)}

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            clear()
        }
    }

    fun onJankenButtonTapped(view: View?){
        /*引数はViewを受け取るように指定。
        渡されるViewオブジェクトはnull安全が保障されていないので、
        データ型の後ろに「？」をつけている。
        */

        val intent = Intent(this, ResultActivity2::class.java)//ResultActivity2.ktを
        intent.putExtra("MY_HAND", view?.id)
        startActivity(intent)
    }
}
