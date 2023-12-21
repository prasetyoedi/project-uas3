package com.example.project_uas3.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.project_uas3.database.model.OrderData
import com.example.project_uas3.R
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private lateinit var orderData: OrderData
    private var formattedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val title = intent.getStringExtra("title")
        val start = intent.getStringExtra("start")
        val end = intent.getStringExtra("end")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        val titleTextView: TextView = findViewById(R.id.title_travel_detail)
        val startTextView: TextView = findViewById(R.id.start_travel_detail)
        val endTextView: TextView = findViewById(R.id.end_travel_detail)
        val priceTextView: TextView = findViewById(R.id.price_travel_detail)
        val descriptionTextView: TextView = findViewById(R.id.description_travel_detail)
        val imageImageView: ImageView = findViewById(R.id.image_travel_detail)

        titleTextView.text = title
        startTextView.text = start
        endTextView.text = end
        priceTextView.text = price
        descriptionTextView.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(imageImageView)

        val orderButton: Button = findViewById(R.id.Order)
        orderButton.setOnClickListener {

            orderData = OrderData(
                title = title.orEmpty(),
                start = start.orEmpty(),
                end = end.orEmpty(),
                price = price.orEmpty(),
                description = description.orEmpty(),
                date = formattedDate,
                imageUrl = imageUrl.orEmpty(),  // Ini sudah diinisialisasi di awal
                orderId = ""
            )

            val historyOrderReference = FirebaseDatabase.getInstance().getReference("HistoryOrder")
            val orderId = historyOrderReference.push().key
            orderData.orderId = orderId.orEmpty()
            historyOrderReference.child(orderId.orEmpty()).setValue(orderData)

            setResult(RESULT_OK)
            finish()
        }

        val datePickerButton: ImageView = findViewById(R.id.datepicker_button)
        datePickerButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time)

                val dateTextView: TextView = findViewById(R.id.date_text_view)
                dateTextView.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }
}
