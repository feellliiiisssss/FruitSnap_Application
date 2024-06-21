package com.dicoding.mybuah

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mybuah.databinding.ActivityDetailTipsBinding
import com.dicoding.mybuah.databinding.ActivityLoginBinding

class DetailTips : AppCompatActivity() {

    companion object{
        const val EXTRA_APP = "extra_app"
    }

    private lateinit var binding: ActivityDetailTipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTipsBinding.inflate((layoutInflater))
        setContentView(binding.root)

        val data_app = intent.getParcelableExtra<Tips>(EXTRA_APP)

        if (data_app != null) {
            binding.judul.text = data_app.judul
            binding.gambar.setImageResource(data_app.gambar)
            binding.tanggal.text = "Diluncurkan pada ${data_app.tanggal}"
            binding.deskripsi.text = data_app.description

        }
    }
}