package com.example.belajarfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import coil.load
import com.example.belajarfirebase.databinding.ActivityAddNoteBinding
import com.example.belajarfirebase.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var noteId: String
    private var imageUri: Uri? = null
    private var imageUrl: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val uid = auth.currentUser?.uid.toString()
        val uuid = UUID.randomUUID()
        noteId = uuid.toString()

        val notes = intent.getParcelableExtra<Notes>(notes)
        Log.d("ISI NOTES", "${notes?.judul}, ${notes?.konten}")

        binding.apply {

            if (notes != null) {
                etJudulNote.setText(notes.judul)
                etKontenNote.setText(notes.konten)
                ivAddNote.load(notes.imageUrl)
            }

            btnAddNotePicture.setOnClickListener {
                val gallery = Intent()
                gallery.type = "image/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                gallery.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(gallery, 101)
            }

            btnAddNote.setOnClickListener {
                val judul = etJudulNote.text.toString()
                val konten = etKontenNote.text.toString()
                if (notes != null) {
                    if (imageUrl != null) {
                        editNote(notes?.noteId.toString(),judul, konten, imageUrl.toString())
                    } else {
                        editNote(notes?.noteId.toString(),judul, konten, notes?.imageUrl.toString())
                    }

                } else {
                    addNote(judul, konten, uid, noteId, imageUrl!!)
                }

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 101){
            imageUri = data?.data
            Log.d("GET PICTURE", "Gallery picture : $imageUri")
            storage = FirebaseStorage.getInstance()
            storageReference = storage.getReference("notes/ $noteId")
            val uploadTask = storageReference.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    imageUrl = it.result
                    Log.d("UPLOAD PICTURE", "Upload picture: $imageUrl")
                    binding.ivAddNote.load(imageUrl)
                }
            }

        }

    }

    private fun addNote(judul: String, konten: String, uid: String, noteId: String, imageUrl: Uri) {
        val notes = Notes(noteId, judul, konten, uid, imageUrl.toString())
        database.collection("notes").document(noteId).set(notes)
            .addOnSuccessListener {
                startActivity(Intent(this, Homepage::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Add note failed!", Toast.LENGTH_SHORT)
                Log.e("NOTES", "$e")
            }
    }
    private fun editNote(noteId: String, judul: String, konten: String, imageUrl: String) {
        database.collection("notes").document(noteId)
            .update("judul", judul, "konten", konten, "imageUrl", imageUrl)
            .addOnSuccessListener {
                startActivity(Intent(this, Homepage::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Add note failed!", Toast.LENGTH_SHORT)
                Log.e("NOTES", "$e")
            }
    }

    companion object {
        const val notes: String ="NOTES"
    }
}