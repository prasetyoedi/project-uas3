package com.example.project_uas3.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.project_uas3.database.model.TravelData
import com.example.project_uas3.databinding.ActivityEditAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdminBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private var originalImageId: String? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgViewEdit.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        val title = binding.txtTitleEdit
        val start = binding.txtStartEdit
        val end = binding.txtEndEdit
        val price = binding.txtPriceEdit
        val description = binding.txtDescriptionEdit

        // Pengecekan apakah data ekstra dengan kunci yang diharapkan ada sebelum mengaksesnya
        val uriString = intent.getStringExtra("imgId")

        if (uriString != null) {
            try {
                // Pengecekan nilai uriString tidak null sebelum parsing Uri
                originalImageId = Uri.parse(uriString).lastPathSegment?.removePrefix("images/")
            } catch (e: Exception) {
                // Handle jika terjadi kesalahan saat parsing Uri
                Log.e("EditAdminActivity", "Error parsing Uri: ${e.message}")
            }
        } else {
            // Handle jika uriString bernilai null atau key not found
            Log.e("EditAdminActivity", "uriString is null or key not found")
        }

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgViewEdit)

        title.setText(intent.getStringExtra("title"))
        start.setText(intent.getStringExtra("start"))
        end.setText(intent.getStringExtra("end"))
        price.setText(intent.getStringExtra("price"))
        description.setText(intent.getStringExtra("description"))

        binding.btnEdit.setOnClickListener {
            uploadData(imageUri)
        }
    }

    private fun uploadData(imageUri: Uri? = null) {
        val updatedTitle = binding.txtTitleEdit.text.toString()
        val updatedStart = binding.txtStartEdit.text.toString()
        val updatedEnd = binding.txtEndEdit.text.toString()
        val updatedPrice = binding.txtPriceEdit.text.toString()
        val updatedDescription = binding.txtDescriptionEdit.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Travel")

        if (imageUri != null) {
            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$originalImageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = TravelData(
                        updatedTitle,
                        updatedStart,
                        updatedEnd,
                        updatedPrice,
                        updatedDescription,
                        imageUrl.toString()
                    )
                    database.child(originalImageId!!).setValue(item)
                        .addOnCompleteListener {
                            clearFieldsAndNavigateToHome()
                            // Handle completion, e.g., show a success message
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            val updatedList = mapOf(
                "title" to updatedTitle,
                "start" to updatedStart,
                "end" to updatedEnd,
                "price" to updatedPrice,
                "description" to updatedDescription
            )

            // Update the data with the new title
            database.child(originalImageId!!).updateChildren(updatedList)
                .addOnCompleteListener {
                    clearFieldsAndNavigateToHome()
                    // Handle completion, e.g., show a success message
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun clearFieldsAndNavigateToHome() {
        binding.txtTitleEdit.text!!.clear()
        binding.txtStartEdit.text!!.clear()
        binding.txtEndEdit.text!!.clear()
        binding.txtPriceEdit.text!!.clear()
        binding.txtDescriptionEdit.text!!.clear()
        startActivity(Intent(this, HomeAdminActivity::class.java))
        finish()
    }
}
