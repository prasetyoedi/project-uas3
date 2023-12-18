package com.example.project_uas3

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : AppCompatActivity() {

    private lateinit var orderData: OrderData
    private var formattedDate: String = "" // Tambahkan inisialisasi di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Get data from the intent
        val title = intent.getStringExtra("title")
        val start = intent.getStringExtra("start")
        val end = intent.getStringExtra("end")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Set the data to the corresponding views in your layout
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

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(this)
            .load(imageUrl)
            .into(imageImageView)

        // Button Order
        val orderButton: Button = findViewById(R.id.Order)
        orderButton.setOnClickListener {
            // Add your order logic here, such as sending the order to the server
            // You can display a confirmation message or navigate to a confirmation screen
            // based on the success of the order process

            // Create an OrderData object with the data
            orderData = OrderData(
                title = title.orEmpty(),
                start = start.orEmpty(),
                end = end.orEmpty(),
                price = price.orEmpty(),
                description = description.orEmpty(),
                date = formattedDate
            )

            // Send the OrderData back to HistoryOrderFragment
            val intent = Intent()
            intent.putExtra("orderData", orderData)
            setResult(RESULT_OK, intent)
            finish()
        }

        // ImageView for the calendar icon
        val datePickerButton: ImageView = findViewById(R.id.datepicker_button)

        // Set OnClickListener for the calendar icon
        datePickerButton.setOnClickListener {
            // Show the DatePicker when the calendar icon is clicked
            showDatePicker()
        }

        // ... (your existing code)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                // Handle the selected date, you can update a TextView or use it as needed
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Format the selected date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time) // Update formattedDate

                // Example: Update a TextView with the selected date
                val dateTextView: TextView = findViewById(R.id.date_text_view)
                dateTextView.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set minimum date (optional)
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        datePicker.show()
    }
}
