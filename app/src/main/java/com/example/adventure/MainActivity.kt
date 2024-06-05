package com.example.adventure

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import network.ApiServiceSalesTerritory
import network.RetrofitClient
import network.SalesTerritory

class MainActivity : AppCompatActivity() {
    private lateinit var etTerritoryId: EditText
    private lateinit var etTerritoryName: EditText
    private lateinit var etCountryRegionCode: EditText
    private lateinit var etTerritoryGroup: EditText
    private lateinit var etBuscarTerritorio: EditText

    private lateinit var eterror : TextView

    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBuscar: Button
    private lateinit var btPersonalTerritory: Button


    private lateinit var apiService: ApiServiceSalesTerritory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etTerritoryId = findViewById(R.id.etTerritoryID)
        etTerritoryName = findViewById(R.id.etName)
        etCountryRegionCode = findViewById(R.id.etCountryRegionCode)
        etTerritoryGroup = findViewById(R.id.etGroup)
        etBuscarTerritorio = findViewById(R.id.etBuscarTerritorio)
        // eterror = findViewById(R.id.error)

        btnAdd = findViewById(R.id.btSave)
        btnUpdate = findViewById(R.id.btUpdate)
        btnDelete = findViewById(R.id.btDelete)
        btnBuscar = findViewById(R.id.btBuscarTerritorio)
        btPersonalTerritory = findViewById(R.id.btPersonalTerritory)

        btPersonalTerritory.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            val idText = etTerritoryId.text.toString()
            val name = etTerritoryName.text.toString()
            val regionCode = etCountryRegionCode.text.toString()
            val group = etTerritoryGroup.text.toString()

            if (idText.isNotEmpty() && name.isNotEmpty() && regionCode.isNotEmpty() && group.isNotEmpty()) {
                val territory = SalesTerritory(
                    territoryId = idText.toInt(),
                    name = name,
                    countryRegionCode = regionCode,
                    groupTerritory = group
                )
                createTerritory(territory)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            val idText = etTerritoryId.text.toString()
            val name = etTerritoryName.text.toString()
            val regionCode = etCountryRegionCode.text.toString()
            val group = etTerritoryGroup.text.toString()

            if (idText.isNotEmpty() && name.isNotEmpty() && regionCode.isNotEmpty() && group.isNotEmpty()) {
                val territory = SalesTerritory(
                    territoryId = idText.toInt(),
                    name = name,
                    countryRegionCode = regionCode,
                    groupTerritory = group
                )
                updateTerritory(idText.toInt(), territory)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val idText = etTerritoryId.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toInt()
                deleteTerritory(id)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }


        btnBuscar.setOnClickListener {
            val idText = etBuscarTerritorio.text.toString()
            if (idText.isNotEmpty()) {
                val id = idText.toInt()
                getTerritoryById(id)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun createTerritory(territory: SalesTerritory) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.createSalesTerritory(territory)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Territorio creado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity, "Error al crear el territorio: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTerritory(id: Int, territory: SalesTerritory) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.updateSalesTerritory(id, territory)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Territorio actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity, "Error al actualizar el territorio: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteTerritory(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.deleteSalesTerritory(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Territorio eliminado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity, "Error al eliminar el territorio: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getTerritoryById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getSalesTerritoryById(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val territory = response.body()
                        if (territory != null) {
                            etTerritoryId.setText(territory.territoryId.toString())
                            etTerritoryName.setText(territory.name)
                            etCountryRegionCode.setText(territory.countryRegionCode)
                            etTerritoryGroup.setText(territory.groupTerritory)
                        } else {
                            Toast.makeText(this@MainActivity, "Territorio no encontrado", Toast.LENGTH_SHORT).show()

                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity, "Error al obtener el territorio: $errorMessage", Toast.LENGTH_SHORT).show()
                        //eterror.setText(errorMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    //eterror.setText(e.message)
                }
            }
        }
    }
}