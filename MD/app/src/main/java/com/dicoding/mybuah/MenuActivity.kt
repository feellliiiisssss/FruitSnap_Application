package com.dicoding.mybuah

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mybuah.R
import com.dicoding.mybuah.databinding.ActivityLoginBinding
import com.dicoding.mybuah.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    companion object {
        const val USER_TOKEN = "user_token"
    }

    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoA.setOnClickListener{detect()}
        binding.logoB.setOnClickListener{tips()}
        binding.logoC.setOnClickListener{about()}
    }

    fun detect() {
        val moveToMain = Intent(this@MenuActivity, MainActivity::class.java)
        moveToMain.putExtra(MainActivity.USER_TOKEN, intent.getStringExtra(MainActivity.USER_TOKEN))
        startActivity(moveToMain)
    }

    fun tips() {
        val moveToTips = Intent(this@MenuActivity, TipsActivity::class.java)
        startActivity(moveToTips)
    }

    fun about() {
        val moveToAbout = Intent(this@MenuActivity, AboutActivity::class.java)
        startActivity(moveToAbout)
    }
}