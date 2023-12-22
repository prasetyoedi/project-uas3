package com.example.project_uas3.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.project_uas3.database.model.TravelData
import com.example.project_uas3.databinding.ActivityAddAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class AddAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAdminBinding

    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgViewAdd.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        binding.btnChooseImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonBack.setOnClickListener{
            onBackPressed()
        }

    }

    private fun uploadData(imageUri: Uri? = null) {
        val title: String = binding.txtTitleAdmin.text.toString()
        val start: String = binding.txtStartAdmin.text.toString()
        val end: String = binding.txtEndAdmin.text.toString()
        val price: String = binding.txtPriceAdmin.text.toString()
        val description: String = binding.txtDescriptionAdmin.text.toString()

        val imageId = UUID.randomUUID().toString()

        if (title.isNotEmpty() && start.isNotEmpty() && end.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty() && imageUri != null) {
            // Generate a unique ID for the image

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
                val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                val item = TravelData(title, start, end, price, description, imageUrl.toString())
                database = FirebaseDatabase.getInstance().getReference("Travel")
                database.child(imageId).setValue(item)
                    .addOnCompleteListener {
                        binding.txtTitleAdmin.text!!.clear()
                        binding.txtStartAdmin.text!!.clear()
                        binding.txtEndAdmin.text!!.clear()
                        binding.txtPriceAdmin.text!!.clear()
                        binding.txtDescriptionAdmin.text!!.clear()
                        Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()


                        // Navigate back to HomeAdminActivity
                        val intent = Intent(this, HomeAdminActivity::class.java)
                        startActivity(intent)
                        finish() // Finish the current activity to prevent going back to it with the back button
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}