package com.example.myapplication


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.DesktopItem
import com.example.myapplication.databinding.ActivityStockFormBinding
import kotlinx.coroutines.launch

class StockFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockFormBinding
    private lateinit var database: AppDatabase
    private var currentItemId: Int = -1 // ID por defecto para saber si es nuevo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        // Verificamos si nos pasaron un ID para editar
        currentItemId = intent.getIntExtra("EXTRA_ITEM_ID", -1)
        if (currentItemId != -1) {
            title = "Editar Modelo 3D"
            loadItemData(currentItemId)
        } else {
            title = "Nuevo Modelo 3D"
        }

        binding.btnSaveStock.setOnClickListener {
            saveOrUpdateItem()
        }
        binding.btnCancelStock.setOnClickListener {
            finish()
        }
    }

    private fun loadItemData(id: Int) {
        lifecycleScope.launch {
            val item = database.desktopDao().getItemById(id)
            item?.let {
                binding.etStockName.setText(it.name)
                binding.etStockMaterial.setText(it.material)
                binding.etStockPrice.setText(it.price.toString())
                binding.etStock.setText(it.stock.toString())
            }
        }
    }

    private fun saveOrUpdateItem() {
        val name = binding.etStockName.text.toString().trim()
        val material = binding.etStockMaterial.text.toString().trim()
        val priceStr = binding.etStockPrice.text.toString().trim()
        val stock = binding.etStock.text.toString().toIntOrNull() ?: 0

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Completá al menos el nombre y el precio", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0

        lifecycleScope.launch {
            if (currentItemId == -1) {
                // Es nuevo -> INSERT
                val newItem = DesktopItem(name = name, material = material, price = price, stock = stock)
                database.desktopDao().insertItem(newItem)
                Toast.makeText(this@StockFormActivity, "¡Modelo agregado al stock!", Toast.LENGTH_SHORT).show()
            } else {
                // Es edición -> UPDATE (mantenemos el ID original)
                val updatedItem = DesktopItem(id = currentItemId, name = name, material = material, price = price, stock = stock)
                database.desktopDao().updateItem(updatedItem)
                Toast.makeText(this@StockFormActivity, "¡Modelo actualizado!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}