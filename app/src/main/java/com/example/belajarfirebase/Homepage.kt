package com.example.belajarfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.belajarfirebase.databinding.ActivityHomepageBinding
import com.example.belajarfirebase.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Homepage : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var email: String
    private lateinit var adapter: NoteAdapter
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        uid = auth.currentUser?.uid.toString()

        getUserData()

        adapter = NoteAdapter(this)

        getNotesData()
        binding.apply {

            rvHomepage.layoutManager = LinearLayoutManager(this@Homepage)

            btnSignOut.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@Homepage, SignIn::class.java))
                finish()
            }
            btnGoAddNote.setOnClickListener {
                startActivity(Intent(this@Homepage, AddNote::class.java))
            }

        }
    }

    private fun getUserData() {
        database.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    email = document.getString("email").toString()
                    binding.tvWelcome.text = "Welcome $email"
                } else {
                    Log.d("GET DATA USER", "No such document")
                }
            }
            .addOnFailureListener { e ->
                Log.d("GET DATA USER", "get failed with ", e)
            }
    }

    private fun getNotesData() {
        database.collection("notes")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    adapter.dataset.add(
                        Notes(
                            noteId = document.getString("noteId").toString(),
                            judul = document.getString("judul").toString(),
                            konten = document.getString("konten").toString(),
                            imageUrl = document.getString("imageUrl").toString(),
                            uid = document.getString("uid").toString()
                        )
                    )
                    binding.rvHomepage.adapter = adapter
                }
            }
    }

    fun deleteAdapter() {
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun notify (savedInstanceState: Bundle?) {

        }
    }
}