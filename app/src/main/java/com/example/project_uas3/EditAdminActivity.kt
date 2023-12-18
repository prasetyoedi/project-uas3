package com.example.project_uas3

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.project_uas3.databinding.ActivityEditAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class EditAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdminBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

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
            // Generate a unique ID for the image
            val imageId = UUID.randomUUID().toString()

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
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
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            // Handle completion, e.g., show a success message
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            // Start HomeAdminActivity after successful update
                            val intent = Intent(this, HomeAdminActivity::class.java)
                            startActivity(intent)

                            // Finish current activity
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no new image is selected, update the data without uploading a new image
            val imageId = intent.getStringExtra("imgId")!!

            val updatedList = mapOf(
                "title" to updatedTitle,
                "start" to updatedStart,
                "end" to updatedEnd,
                "price" to updatedPrice,
                "description" to updatedDescription
            )

            // Update the data with the new title
            database.child(imageId).updateChildren(updatedList)
                .addOnCompleteListener {
                    // Handle completion, e.g., show a success message
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()

                    // Start HomeAdminActivity after successful update
                    val intent = Intent(this, HomeAdminActivity::class.java)
                    startActivity(intent)

                    // Finish current activity
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
