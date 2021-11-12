package com.example.belajarfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.belajarfirebase.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        reload()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        binding.apply {


            btnGoToSignUp.setOnClickListener {
                startActivity(Intent(this@SignIn, SignUp::class.java))
            }

            btnSignIn.setOnClickListener {
                val emailSignin = binding.etEmailSignIn.text.toString()
                val passwordSignin = binding.etPasswordSignIn.text.toString()
                signIn(emailSignin, passwordSignin)
            }
        }
    }

    private fun signIn(emailSignin: String, password: String) {
        val uid = auth.currentUser?.uid.toString()
        auth.signInWithEmailAndPassword(emailSignin, password).addOnSuccessListener {
            Log.d("SIGN IN", "$uid udah sign in!")
            Toast.makeText(this, "Sign In success!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Homepage::class.java))
        }
            .addOnFailureListener {
                Log.d("SIGN IN", "$uid gagal sign in!")
                Toast.makeText(this, "Sign In failed!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reload() {
        if (auth.currentUser != null) {
            startActivity(Intent(this, Homepage::class.java))
        }
    }
}