package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.databinding.FragmentStockBinding
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest

class StockFragment : Fragment() {

    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!

    private lateinit var desktopAdapter: DesktopAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())

        // Inicializamos el adaptador pasando la lista inicial y los dos callbacks (click y toque largo)
        desktopAdapter = DesktopAdapter(
            stockList = emptyList(),
            onClick = { item ->
                // Al hacer click normal, abrimos el formulario pasando el ID para editar
                val intent = Intent(requireContext(), StockFormActivity::class.java).apply {
                    putExtra("EXTRA_ITEM_ID", item.id)
                }
                startActivity(intent)
            },
            onLongClick = { item ->
                // Toque largo para eliminar con confirmación
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar del stock")
                    .setMessage("¿Querés eliminar ${item.name} del inventario?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        lifecycleScope.launch {
                            database.desktopDao().deleteItem(item)
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        binding.recyclerViewStock.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = desktopAdapter
        }

        observeStock()
    }

    private fun observeStock() {
        viewLifecycleOwner.lifecycleScope.launch {
            database.desktopDao().getAllStock().collectLatest { stockList ->
                desktopAdapter.updateStock(stockList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}