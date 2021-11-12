package com.example.belajarfirebase

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.belajarfirebase.databinding.NotesCardBinding
import com.example.belajarfirebase.model.Notes
import com.google.firebase.firestore.FirebaseFirestore

class NoteAdapter (val activity : Homepage) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    var dataset = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            NotesCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position], dataset, position, activity)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder(val binding: NotesCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var database: FirebaseFirestore = FirebaseFirestore.getInstance()
        fun bind (data : Notes, dataset: ArrayList<Notes>, position: Int, activity: Homepage) {
            binding.apply {
                tvJudulCardViewNote.text = data.judul
                tvKontenNoteCard.text = data.konten

                btnDeleteNoteCard.setOnClickListener {
                    database.collection("notes").document(data.noteId).delete()
                    dataset.removeAt(position)
                    activity.deleteAdapter()
                }
                btnEditNoteCard.setOnClickListener {
                    val intent = Intent(itemView.context, AddNote::class.java)
                    intent.putExtra(AddNote.notes, data)
                    itemView.context.startActivity(intent)
                }

                ivNoteCard.load(data.imageUrl)
            }
        }
    }

}