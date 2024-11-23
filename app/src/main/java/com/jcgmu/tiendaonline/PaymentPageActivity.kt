package com.jcgmu.tiendaonline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.epayco.android.Epayco
import co.epayco.android.models.Authentication
import co.epayco.android.models.Card
import co.epayco.android.models.Charge
import co.epayco.android.util.EpaycoCallback
import org.json.JSONObject

class PaymentPageActivity : AppCompatActivity() {

    private lateinit var tarjetaLayout: LinearLayout
    private lateinit var tarjetaRadioButton: RadioButton
    private lateinit var metodoPagoGroup: RadioGroup

    private lateinit var tarjetaEditText: EditText
    private lateinit var cvcEditText: EditText
    private lateinit var expMonthEditText: EditText
    private lateinit var expYearEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var docNumberEditText: EditText
    private lateinit var pagarButton: Button

    private var total: Double = 0.0 // Valor total recibido desde CheckoutActivity
    private var alertDialog: AlertDialog? = null // Objeto global para el diálogo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)

        // Recibir el valor total desde CheckoutActivity
        total = intent.getDoubleExtra("total", 0.0)

        tarjetaLayout = findViewById(R.id.tarjetaLayout)
        tarjetaRadioButton = findViewById(R.id.tarjetaRadioButton)
        metodoPagoGroup = findViewById(R.id.metodoPagoGroup)

        tarjetaEditText = findViewById(R.id.tarjetaEditText)
        cvcEditText = findViewById(R.id.cvcEditText)
        expMonthEditText = findViewById(R.id.expMonthEditText)
        expYearEditText = findViewById(R.id.expYearEditText)
        nameEditText = findViewById(R.id.nameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        docNumberEditText = findViewById(R.id.docNumberEditText)
        pagarButton = findViewById(R.id.pagarButton)

        metodoPagoGroup.setOnCheckedChangeListener { _, checkedId ->
            tarjetaLayout.visibility =
                if (checkedId == R.id.tarjetaRadioButton) LinearLayout.VISIBLE else LinearLayout.GONE
        }

        pagarButton.setOnClickListener {
            if (tarjetaRadioButton.isChecked) {
                procesarPagoConTarjeta()
            } else {
                mostrarAlerta("Por favor selecciona un método de pago.")
            }
        }
    }

    private fun procesarPagoConTarjeta() {
        val numeroTarjeta = tarjetaEditText.text.toString()
        val cvc = cvcEditText.text.toString()
        val expMonth = expMonthEditText.text.toString()
        val expYear = expYearEditText.text.toString()
        val name = nameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val docNumber = docNumberEditText.text.toString()

        if (numeroTarjeta.isEmpty() || cvc.isEmpty() || expMonth.isEmpty() || expYear.isEmpty() ||
            name.isEmpty() || lastName.isEmpty() || email.isEmpty() || docNumber.isEmpty()
        ) {
            mostrarAlerta("Completa todos los campos.")
            return
        }

        val auth = Authentication().apply {
            apiKey = "PUBLIC API KEY" // PUBLIC_KEY
            privateKey = "PRIVATE API KEY" // PRIVATE_KEY
            lang = "ES"
            test = true
        }

        val epayco = Epayco(auth)

        val card = Card().apply {
            this.number = numeroTarjeta
            this.cvc = cvc
            this.month = expMonth
            this.year = expYear
        }

        // Crear un token primero
        epayco.createToken(card, object : EpaycoCallback {
            override fun onSuccess(data: JSONObject) {
                Log.d("ePayco", "Token generado exitosamente: $data")

                val tokenId = data.optJSONObject("data")?.optString("id")
                if (!tokenId.isNullOrEmpty()) {
                    realizarPago(tokenId, name, lastName, email, docNumber)
                } else {
                    mostrarAlerta("Error al generar el token de la tarjeta.")
                }
            }

            override fun onError(error: Exception) {
                Log.e("ePayco", "Error al generar el token: ${error.message}")
                mostrarAlerta("Error al generar el token: ${error.message}")
            }
        })
    }

    private fun realizarPago(tokenId: String, name: String, lastName: String, email: String, docNumber: String) {
        val uniqueInvoice = "INV-${System.currentTimeMillis()}" // Generar referencia única
        val tax = (total * 0.19).toString() // Calcular IVA (19%)
        val taxBase = (total - total * 0.19).toString() // Calcular base imponible

        val charge = Charge().apply {
            this.tokenCard = tokenId
            this.customerId = "CUSTOMER_ID" // Cambiar por un ID de cliente válido
            this.docType = "CC"
            this.docNumber = docNumber
            this.name = name
            this.lastName = lastName
            this.email = email
            this.invoice = uniqueInvoice
            this.description = "Compra en TiendaOnline"
            this.value = total.toString() // Usar el valor total dinámico
            this.tax = tax // IVA dinámico
            this.taxBase = taxBase
            this.currency = "COP"
            this.dues = "1" // Número de cuotas
            this.ip = "190.000.000.000" // IP del cliente
            this.urlResponse = "https://secure.payco.co/restpagos/testRest/endpagopse.php"
            this.urlConfirmation = "https://secure.payco.co/restpagos/testRest/endpagopse.php"
        }

        val auth = Authentication().apply {
            apiKey = "66e5c6208d972f144c7f93920d9b1445"
            privateKey = "c8111251444427b6a03231275f2702a2"
            lang = "ES"
            test = true
        }

        val epayco = Epayco(auth)

        epayco.createCharge(charge, object : EpaycoCallback {
            override fun onSuccess(data: JSONObject) {
                Log.d("ePayco", "Pago realizado exitosamente: $data")
                mostrarAlerta("Pago realizado exitosamente: ${data.optString("ref_payco")}")
                redirigirAListadoProductos()
            }

            override fun onError(error: Exception) {
                Log.e("ePayco", "Error al procesar el pago: ${error.message}")
                mostrarAlerta("Error al procesar el pago: ${error.message}")
            }
        })
    }

    private fun mostrarAlerta(mensaje: String) {
        if (!isFinishing && !isDestroyed) {
            runOnUiThread {
                if (alertDialog == null || alertDialog?.isShowing == false) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Información")
                    builder.setMessage(mensaje)
                    builder.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
                    alertDialog = builder.create()
                    alertDialog?.show()
                }
            }
        } else {
            Log.w("PaymentPageActivity", "No se puede mostrar el diálogo: Actividad destruida o finalizada.")
        }
    }

    private fun redirigirAListadoProductos() {
        val intent = Intent(this, ListadoProductosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
        alertDialog = null
    }
}
