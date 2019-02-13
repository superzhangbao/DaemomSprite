package com.xiaolan.daemomsprite

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.xiaolan.daemomsprite.util.SilentInstall
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

private val MainActivity.sharedPreferences: SharedPreferences?
    get() {
        return getSharedPreferences("info", 0)
    }

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var apkPath = "/mnt/internal_sd/test2.0.apk"
    @SuppressLint("CommitPrefEdits")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start -> {
                val edit = sharedPreferences?.edit()
                edit?.putBoolean("flag", true)
                edit?.apply()
                val boolean = sharedPreferences?.getBoolean("flag", false)
                if (boolean!!) {
                    tv.text = "守护已打开"
                } else {
                    tv.text = "守护已关闭"
                }
                startService(Intent(this@MainActivity, MainService::class.java))
            }
            R.id.stop -> {
                val edit = sharedPreferences?.edit()
                edit?.putBoolean("flag", false)
                edit?.apply()
                val boolean = sharedPreferences?.getBoolean("flag", false)
                if (boolean!!) {
                    tv.text = "守护已打开"
                } else {
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
        val edit = sharedPreferences?.edit()
        edit?.putBoolean("flag", true)
        edit?.apply()
        startService(Intent(this@MainActivity, MainService::class.java))
        startService(Intent(this@MainActivity, ProtectService::class.java))
        val boolean = sp?.getBoolean("flag", false)
        if (boolean!!) {
            tv.text = "守护已打开"
        } else {
            tv.text = "守护已关闭"
        }

        tv_apk.text = apkPath
        tv_version.text = BuildConfig.VERSION_NAME
    }

    fun onSilentInstall(view: View) {
        if (!isRoot()) {
            Toast.makeText(this, "没有ROOT权限，不能使用秒装", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(apkPath)) {
            Toast.makeText(this, "请选择安装包！", Toast.LENGTH_SHORT).show()
            return
        }
        val button = view as Button
        button.text = "安装中"
        Thread(Runnable {
            val installHelper = SilentInstall()
            val result = installHelper.install(apkPath)
            runOnUiThread {
                if (result) {
                    Toast.makeText(this@MainActivity, "安装成功！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "安装失败！", Toast.LENGTH_SHORT).show()
                }
                button.text = "秒装"
            }
        }).start()

    }

    fun onForwardToAccessibility(view: View) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

    fun onSmartInstall(view: View) {
        if (TextUtils.isEmpty(apkPath)) {
            Toast.makeText(this, "请选择安装包！", Toast.LENGTH_SHORT).show()
            return
        }
        val uri = Uri.fromFile(File(apkPath))
        val localIntent = Intent(Intent.ACTION_VIEW)
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(localIntent)
    }

    /**
     * 判断手机是否拥有Root权限。
     * @return 有root权限返回true，否则返回false。
     */
    fun isRoot(): Boolean {
        var bool = false
        try {
            bool = File("/system/bin/su").exists() || File("/system/xbin/su").exists()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bool
    }
}
