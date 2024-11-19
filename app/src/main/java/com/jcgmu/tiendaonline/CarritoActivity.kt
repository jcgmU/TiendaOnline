package com.jcgmu.tiendaonline

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.epayco.android.Epayco
import co.epayco.android.models.Authentication
import co.epayco.android.models.Card
import co.epayco.android.models.Charge
import co.epayco.android.util.EpaycoCallback
import org.json.JSONObject

class CarritoActivity : AppCompatActivity() {

    private lateinit var cardNumberEditText: EditText
    private lateinit var cvcEditText: EditText
    private lateinit var expYearEditText: EditText
    private lateinit var expMonthEditText: EditText
    private lateinit var pagarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        // Inicializar EditTexts
        cardNumberEditText = findViewById(R.id.card_number)
        cvcEditText = findViewById(R.id.cvc)
        expYearEditText = findViewById(R.id.exp_year)
        expMonthEditText = findViewById(R.id.exp_month)
        pagarButton = findViewById(R.id.pagarButton)

        // Configurar autenticación
        val auth = Authentication().apply {
            apiKey = "PUBLIC API KEY"
            privateKey = "PRIVATE API KEY"
            lang = "ES"
            test = true
        }

        val epayco = Epayco(auth)

        pagarButton.setOnClickListener {
            // Validar los datos de la tarjeta
            val number = cardNumberEditText.text.toString()
            val cvc = cvcEditText.text.toString()
            val expYear = expYearEditText.text.toString()
            val expMonth = expMonthEditText.text.toString()

            if (number.isEmpty() || cvc.isEmpty() || expYear.isEmpty() || expMonth.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear objeto Card con nombres de propiedad correctos
            val card = Card().apply {
                this.number = number
                this.cvc = cvc
                this.month = expMonth // Usar 'month'
                this.year = expYear   // Usar 'year'
                //this.hasCvv = true
            }

            // Crear Token
            epayco.createToken(card, object : EpaycoCallback {
                override fun onSuccess(data: JSONObject) {
                    val tokenId = data.getString("id")
                    // Aquí puedes crear el cargo utilizando el token
                    crearCargo(epayco, tokenId)
                }

                override fun onError(error: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@CarritoActivity, "Error al crear el token: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun crearCargo(epayco: Epayco, tokenId: String) {
        // Configura los datos del cargo
        val charge = Charge().apply {
            tokenCard = tokenId
            customerId = "1519417"
            docType = "CC"
            docNumber = "1035851980"
            name = "John"
            lastName = "Doe"
            email = "example@email.com"
            invoice = "OR-1234"
            description = "Test Payment"
            value = "116000"
            tax = "16000"
            taxBase = "100000"
            currency = "COP"
            dues = "12"
            ip = "190.000.000.000"
        }

        // Realizar el cobro
        epayco.createCharge(charge, object : EpaycoCallback {
            override fun onSuccess(data: JSONObject) {
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "Pago realizado con éxito", Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(error: Exception) {
                runOnUiThread {
                    Toast.makeText(this@CarritoActivity, "Error al realizar el pago: ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}