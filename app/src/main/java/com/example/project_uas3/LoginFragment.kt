package com.example.project_uas3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_uas3.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        with(binding) {
            loginBtn.setOnClickListener {
                val email = email.text.toString()
                val password = password.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Save login status and user info to SharedPreferences
                                saveLoginStatus(true)
                                saveUserInfoToSharedPreferences(email)

                                // Navigate to HomeActivity or AdminActivity based on userType
                                navigateToUserOrAdmin(email)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Login failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun saveUserInfoToSharedPreferences(email: String) {
        val username = getUsernameFromEmail(email)
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        // Add additional commands to save other user information if needed
        editor.apply()
    }

    private fun navigateToUserOrAdmin(email: String) {
        val userType = getUserTypeFromEmail(email)
        val username = getUsernameFromEmail(email)

        val intent = if (userType == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)
        } else {
            Intent(requireContext(), NavigationActivity::class.java)
        }

        saveUsername(username)

        startActivity(intent)
        requireActivity().finish()
    }

    private fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        editor.apply()
    }

    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }

    private fun getUserTypeFromEmail(email: String): String {
        return if (email.contains("admin")) {
            "admin"
        } else {
            "user"
        }
    }
}
