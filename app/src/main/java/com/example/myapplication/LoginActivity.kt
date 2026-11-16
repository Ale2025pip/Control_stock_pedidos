package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.User
import com.example.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        // Botón de Iniciar Sesión
        binding.btnLogin.setOnClickListener {
            val userStr = binding.etUser.text.toString().trim()
            val passStr = binding.etPassword.text.toString().trim()

            if (userStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val foundUser = database.userDao().login(userStr, passStr)

                // Permitimos el admin por defecto o un usuario registrado en Room
                if (foundUser != null || (userStr == "admin" && passStr == "1234")) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón que abre el diálogo de registro
        binding.btnOpenRegister.setOnClickListener {
            showRegisterDialog()
        }
    }

    private fun showRegisterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null)
        val etRegUser = dialogView.findViewById<EditText>(R.id.etRegUser)
        val etRegPassword = dialogView.findViewById<EditText>(R.id.etRegPassword)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Registro")
            .setView(dialogView)
            .setPositiveButton("Registrar") { _, _ ->
                val newUser = etRegUser.text.toString().trim()
                val newPass = etRegPassword.text.toString().trim()

                if (newUser.isNotEmpty() && newPass.isNotEmpty()) {
                    lifecycleScope.launch {
                        // Guardamos el usuario en la base de datos Room
                        database.userDao().insertUser(User(username = newUser, password = newPass))
                        Toast.makeText(this@LoginActivity, "¡Usuario registrado con éxito! Ya podés iniciar sesión.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}