package com.example.belajarfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.belajarfirebase.databinding.ActivitySignUpBinding
import com.example.belajarfirebase.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        var emailSignup: String
        var passwordSignup: String

        binding.apply {

            btnSignUp.setOnClickListener {
                emailSignup = etEmailSignUp.text.toString()
                passwordSignup = etPasswordSignUp.text.toString()
                createAccount(emailSignup, passwordSignup)
            }
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            val uid = auth.currentUser?.uid.toString()
            database.collection("users").document(uid).set(Users(email, uid))
                .addOnSuccessListener {
                    Log.d("JANCOK", "Users $uid successfully sign up!")
                    val intent = Intent(this, SignIn::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Log.e("JANCOK", "$uid ga masuk goblok!!")
                }
        }
    }
}