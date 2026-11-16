package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuramos el ViewPager con las pestañas
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Títulos de las pestañas
        val titles = arrayOf("Stock 3D", "Pedidos")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        // Botón flotante dinámico según la pestaña actual
        binding.fabAdd.setOnClickListener {
            val currentTab = binding.viewPager.currentItem
            if (currentTab == 0) {
                // Pestaña Stock 3D -> Abre formulario de Stock
                val intent = Intent(this, StockFormActivity::class.java)
                startActivity(intent)
            } else {
                // Pestaña Pedidos -> Abre formulario de Pedidos
                val intent = Intent(this, OrderFormActivity::class.java)
                startActivity(intent)
            }
        }
    }
}