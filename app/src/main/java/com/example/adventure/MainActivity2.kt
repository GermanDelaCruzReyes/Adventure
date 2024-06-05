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
import network.RetrofitClientPerson
import network.SalesPerson
import retrofit2.Response


class MainActivity2 : AppCompatActivity() {
    private lateinit var etBuscarPersonal: EditText
    private lateinit var etBusinessEntityID: EditText
    private lateinit var etTerritoryID: EditText
    private lateinit var etSalesQuota: EditText
    private lateinit var etBonus: EditText
    private lateinit var etCommissionPct: EditText
    private lateinit var btBuscarPersonal: Button
    private lateinit var btAsignarPersonal: Button
    private lateinit var btUpdate: Button
    private lateinit var btDelete: Button
    private lateinit var btBack: Button
    private lateinit var tverror : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        etBuscarPersonal = findViewById(R.id.etBuscarPersonal)
        etBusinessEntityID = findViewById(R.id.etBusinessEntityID)
        etTerritoryID = findViewById(R.id.etTerritoryID)
        etSalesQuota = findViewById(R.id.etSalesQuota)
        etBonus = findViewById(R.id.etBonus)
        etCommissionPct = findViewById(R.id.etCommissionPct)
        btBuscarPersonal = findViewById(R.id.btBuscarPersonal)
        btAsignarPersonal = findViewById(R.id.btnAsignarPersonal)
        btUpdate = findViewById(R.id.btUpdate)
        btDelete = findViewById(R.id.btDelete)
        btBack = findViewById(R.id.btBack)
        //tverror = findViewById(R.id.error)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btBuscarPersonal.setOnClickListener {
            val id = etBuscarPersonal.text.toString().toIntOrNull()
            if (id != null) {
                getSalesPersonById(id)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        btAsignarPersonal.setOnClickListener {
            if (validateFields()) {
                val salesPerson = createSalesPersonFromInput()
                createSalesPerson(salesPerson)
            }
        }

        btUpdate.setOnClickListener {
            val id = etBusinessEntityID.text.toString().toIntOrNull()
            if (id != null && validateFields()) {
                val salesPerson = createSalesPersonFromInput()
                updateSalesPerson(id, salesPerson)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido y complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btDelete.setOnClickListener {
            val id = etBusinessEntityID.text.toString().toIntOrNull()
            if (id != null) {
                deleteSalesPerson(id)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        return when {
            etBusinessEntityID.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor, ingrese el ID de Entidad", Toast.LENGTH_SHORT).show()
                false
            }
            etTerritoryID.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor, ingrese el ID de Territorio", Toast.LENGTH_SHORT).show()
                false
            }
            etSalesQuota.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor, ingrese la Cuota por Venta", Toast.LENGTH_SHORT).show()
                false
            }
            etBonus.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor, ingrese la Bonificación", Toast.LENGTH_SHORT).show()
                false
            }
            etCommissionPct.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Por favor, ingrese el Porcentaje de Comisión", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun getSalesPersonById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClientPerson.instance.getSalesPersonById(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val salesPerson = response.body()
                        etBusinessEntityID.setText(salesPerson?.businessEntityId.toString())
                        etTerritoryID.setText(salesPerson?.territoryId.toString())
                        etSalesQuota.setText(salesPerson?.salesQuota.toString())
                        etBonus.setText(salesPerson?.bonus.toString())
                        etCommissionPct.setText(salesPerson?.commissionPct.toString())
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity2, "Error al buscar el personal: $errorMessage", Toast.LENGTH_SHORT).show()
                        //tverror.text = errorMessage
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity2, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    //tverror.text = e.message
                }
            }
        }
    }

    private fun createSalesPersonFromInput(): SalesPerson {
        return SalesPerson(
            businessEntityId = etBusinessEntityID.text.toString().toInt(),
            territoryId = etTerritoryID.text.toString().toInt(),
            salesQuota = etSalesQuota.text.toString().toDouble(),
            bonus = etBonus.text.toString().toDouble(),
            commissionPct = etCommissionPct.text.toString().toDouble()
        )
    }

    private fun createSalesPerson(salesPerson : SalesPerson) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClientPerson.instance.createSalesPerson(salesPerson)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Personal asignado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity2, "Error al asignar personal: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity2, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateSalesPerson(id: Int, salesPerson: SalesPerson) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClientPerson.instance.updateSalesPerson(id, salesPerson)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Personal actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity2, "Error al actualizar personal: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity2, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteSalesPerson(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClientPerson.instance.deleteSalesPerson(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity2, "Personal eliminado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                        Toast.makeText(this@MainActivity2, "Error al eliminar personal: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity2, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}