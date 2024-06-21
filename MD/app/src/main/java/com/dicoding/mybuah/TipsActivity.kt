package com.dicoding.mybuah

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TipsActivity : AppCompatActivity() {

    private lateinit var rvBuah: RecyclerView
    private val list = ArrayList<Tips>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        rvBuah = findViewById(R.id.rv_buah)
        rvBuah.setHasFixedSize(true)

        list.addAll(getListTips())
        showRecyclerList()
    }

    private fun getListTips(): ArrayList<Tips> {
        val dataName = resources.getStringArray(R.array.data_judul)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_gambar)
        val dataTanggal = resources.getStringArray(R.array.tanggal)
        val listTips = ArrayList<Tips>()
        for (i in dataName.indices) {
            val tips = Tips(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1), dataTanggal[i] )
            listTips.add(tips)
        }
        return listTips
    }

    private fun showRecyclerList() {
        rvBuah.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = BuahAdapterAdapter(list)
        rvBuah.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : BuahAdapterAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Tips) {
                showSelectedTips(data)
            }
        })
    }

    private fun showSelectedTips(hero: Tips) {

        val movePage = Intent(this@TipsActivity,DetailTips::class.java)
        movePage.putExtra(DetailTips.EXTRA_APP,hero)
        startActivity(movePage)
    }

}