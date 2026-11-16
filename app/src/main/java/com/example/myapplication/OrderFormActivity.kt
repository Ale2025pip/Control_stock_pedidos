package com.example.myapplication
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.OrderModel
import com.example.myapplication.databinding.ActivityOrderFormBinding
import kotlinx.coroutines.launch

class OrderFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderFormBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Cálculo de la seña (50%)
        binding.etTotalPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val totalStr = s.toString()
                if (totalStr.isNotEmpty()) {
                    val total = totalStr.toDoubleOrNull() ?: 0.0
                    val deposit = total / 2.0
                    binding.tvDeposit.text = "Seña (50%): \$$deposit"
                } else {
                    binding.tvDeposit.text = "Seña (50%): \$0.00"
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Guardar pedido
        binding.btnSaveOrder.setOnClickListener {
            val client = binding.etClientName.text.toString().trim()
            val product = binding.etProductName.text.toString().trim()
            val desc = binding.etDescription.text.toString().trim()
            val totalStr = binding.etTotalPrice.text.toString().trim()

            if (client.isEmpty() || product.isEmpty() || totalStr.isEmpty()) {
                Toast.makeText(this, "Complete al menos el cliente, producto y precio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = totalStr.toDoubleOrNull() ?: 0.0
            val advance = total / 2.0 // Seña del 50%

            val newOrder = OrderModel(
                clientName = client,
                description = "[$product] $desc",
                deliveryDate = "A coordinar", // O podés agregar un EditText para la fecha si querés
                totalPrice = total,
                advancePayment = advance,
                status = "Pendiente"
            )

            lifecycleScope.launch {
                database.orderDao().insertOrder(newOrder)
                Toast.makeText(this@OrderFormActivity, "¡Pedido guardado con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
