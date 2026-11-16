package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentOrdersBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())

        // Configuramos el RecyclerView
        orderAdapter = OrderAdapter(
            ordersList = emptyList(),
            onLongClick = { order ->
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar pedido")
                    .setMessage("¿Querés eliminar el pedido de ${order.clientName}?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        lifecycleScope.launch {
                            database.orderDao().deleteOrder(order)
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )
        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        // Escuchamos los cambios de la base de datos en tiempo real
        observeOrders()
    }

    private fun observeOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            database.orderDao().getAllOrders().collectLatest { ordersList ->
                orderAdapter.updateOrders(ordersList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}