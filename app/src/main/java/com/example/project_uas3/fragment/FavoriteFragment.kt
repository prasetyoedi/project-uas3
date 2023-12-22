package com.example.project_uas3.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_uas3.database.model.Akun
import com.example.project_uas3.activity.NavigationActivity
import com.example.project_uas3.databinding.FragmentFavoriteBinding
import com.example.project_uas3.adapter.FavoriteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executors


class FavoriteFragment : Fragment() {
    private val binding by lazy {
        FragmentFavoriteBinding.inflate(layoutInflater)
    }

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = (requireActivity() as NavigationActivity).getDatabase()
        val dao = db.favoriteDao()!!

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = firebaseAuth.currentUser?.uid
        userId?.let { uid ->
            firestore.collection("akun")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val email = document.get("email").toString()
                    val account = document.toObject(Akun::class.java)

                    account?.let {
                        if (view != null) {
                            dao.getUserFavourites(account.email).observe(viewLifecycleOwner) { favorites ->
                                favorites?.let {
                                    binding.rvFavorite.apply {
                                        adapter = FavoriteAdapter(it) { favorite ->
                                            Executors.newSingleThreadExecutor().execute {
                                                dao.delete(favorite)
                                            }
                                        }
                                        layoutManager = LinearLayoutManager(context)
                                    }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error loading user data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("FavoriteFragment", "Error loading user data", e)
                }
        }
    }
}
