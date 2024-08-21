package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.Adapters.ChatAdapter
import com.example.whatsapp.databinding.ActivityGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val message: ArrayList<com.example.whatsapp.Models.Message> = ArrayList()

        val senderId: String? = auth.uid
        binding.userName1.text = "Friend's Group"

        val adapter: ChatAdapter = ChatAdapter(message, this)
        binding.chatRecyclerView.adapter = adapter

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        database.reference.child("Group Chat").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                message.clear()
                snapshot.children.forEach {dataSnapshot ->
                    val model= dataSnapshot.getValue(com.example.whatsapp.Models.Message::class.java)
                    model?.let { message.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.send.setOnClickListener {
            val message : String = binding.etMessage.text.toString()
            val model:com.example.whatsapp.Models.Message=com.example.whatsapp.Models.Message(senderId,message)

            model.timestamp = System.currentTimeMillis()

            binding.etMessage.setText("")

            database.reference.child("Group Chat").push().setValue(model).addOnSuccessListener {

            }
        }
    }
}