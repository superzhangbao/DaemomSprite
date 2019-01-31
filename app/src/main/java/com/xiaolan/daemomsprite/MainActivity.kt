package com.xiaolan.daemomsprite

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

private val MainActivity.sharedPreferences: SharedPreferences?
    get() {
        return getSharedPreferences("info", 0)
    }

class MainActivity : AppCompatActivity(), View.OnClickListener {
    @SuppressLint("CommitPrefEdits")
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.start->{
                val edit = sharedPreferences?.edit()
                edit?.putBoolean("flag",true)
                edit?.apply()
                val boolean = sharedPreferences?.getBoolean("flag", false)
                if (boolean!!) {
                    tv.text = "守护已打开"
                }else {
                    tv.text = "守护已关闭"
                }
            }
            R.id.stop->{val edit = sharedPreferences?.edit()
                edit?.putBoolean("flag",false)
                edit?.apply()
                val boolean = sharedPreferences?.getBoolean("flag", false)
                if (boolean!!) {
                    tv.text = "守护已打开"
                }else {
                    tv.text = "守护已关闭"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener(this)
        stop.setOnClickListener(this)
        val sp = sharedPreferences
        val boolean = sp?.getBoolean("flag", false)
        if (boolean!!) {
            tv.text = "守护已打开"
        }else {
            tv.text = "守护已关闭"
        }
        startService(Intent(this@MainActivity, MainService::class.java))
        startService(Intent(this@MainActivity, ProtectService::class.java))
    }
}
