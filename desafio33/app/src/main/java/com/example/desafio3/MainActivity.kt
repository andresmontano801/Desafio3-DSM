package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio3.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.desafio3.UpdateResourceActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var resourceAdapter: ResourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        getAllResources()

        // Búsqueda por ID
        binding.searchButton.setOnClickListener {
            val resourceId = binding.searchEditText.text.toString()
            if (resourceId.isNotEmpty()) {
                getResourceById(resourceId)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para agregar un recurso
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddResourceActivity::class.java)
            startActivity(intent)
        }

        // Botón para eliminar un recurso
        binding.deleteButton.setOnClickListener {
            val resourceId = binding.searchEditText.text.toString()
            if (resourceId.isNotEmpty()) {
                deleteResource(resourceId)
            } else {
                Toast.makeText(this, "Ingrese un ID válido para eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para modificar un recurso
        binding.updateButton.setOnClickListener {
            val resourceId = binding.searchEditText.text.toString()
            if (resourceId.isNotEmpty()) {
                val intent = Intent(this, UpdateResourceActivity::class.java)
                intent.putExtra("RESOURCE_ID", resourceId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Ingrese un ID válido para modificar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAllResources() {
        val service = RetrofitClient.instance.create(ResourceService::class.java)
        service.getAllResources().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    val resources = response.body() ?: emptyList()
                    resourceAdapter = ResourceAdapter(resources)
                    binding.recyclerView.adapter = resourceAdapter
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getResourceById(id: String) {
        val service = RetrofitClient.instance.create(ResourceService::class.java)
        service.getResourceById(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val resource = response.body()
                    resource?.let {
                        resourceAdapter = ResourceAdapter(listOf(it))
                        binding.recyclerView.adapter = resourceAdapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Recurso no encontrado", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun deleteResource(id: String) {
        val service = RetrofitClient.instance.create(ResourceService::class.java)
        service.deleteResource(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso eliminado con éxito", Toast.LENGTH_LONG).show()
                    getAllResources()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
