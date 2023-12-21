package com.example.project_uas3.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project_uas3.Akun
import com.example.project_uas3.activity.HomeAdminActivity
import com.example.project_uas3.activity.NavigationActivity
import com.example.project_uas3.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            regisBtn.setOnClickListener {
                val email = edtEmail.text.toString().trim()
                val username = edtUsername.text.toString().trim()
                val phone = edtPhone.text.toString().trim()
                val password = edtPass.text.toString()

                if (email.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Save user data to Firestore
                                val newAccount = Akun(email, username, password, phone,"user")
                                saveUserDataToFirestore(newAccount)

                                // Save login status to SharedPreferences
                                saveLoginStatus(true)

                                // Navigate to HomeActivity or AdminActivity based on userType
                                navigateToHomeOrAdmin("user")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("RegisterFragment", "Error creating user", task.exception)
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserDataToFirestore(akun: Akun) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { uid ->
            firestore.collection("akun")
                .document(uid)
                .set(akun) // Menggunakan set() untuk menggantikan add() agar dapat menggunakan UID sebagai ID dokumen
                .addOnSuccessListener {
                    Log.d("RegisterFragment", "DocumentSnapshot added with ID: $uid")
                }
                .addOnFailureListener { e ->
                    Log.e("RegisterFragment", "Error adding document to Firestore", e)
                    Toast.makeText(
                        requireContext(),
                        "Error adding document to Firestore: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun navigateToHomeOrAdmin(userType: String) {
        val intent = if (userType == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)
        } else {
            Intent(requireContext(), NavigationActivity::class.java)
        }

        startActivity(intent)
        requireActivity().finish()
    }
}
