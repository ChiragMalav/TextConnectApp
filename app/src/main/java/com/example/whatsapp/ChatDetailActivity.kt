package com.example.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.Adapters.ChatAdapter
import com.example.whatsapp.databinding.ActivityChatDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ChatDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatDetailBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        var senderId: String? = auth.uid
        var receiverId: String? = intent.getStringExtra("userId")
        var userName: String? = intent.getStringExtra("userName")
        var profilePic: String? = intent.getStringExtra("profilePic")

        binding.userName1.setText(userName)
        Picasso.get().load(profilePic).placeholder(R.drawable.baseline_person_24)
            .into(binding.profileImage)

        binding.backArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val message: ArrayList<com.example.whatsapp.Models.Message> = ArrayList()

        val chatAdapter: ChatAdapter = ChatAdapter(message, this)
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val senderRoom: String = senderId + receiverId
        val receiverRoom: String = receiverId + senderId

        database.reference.child("chats").child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    message.clear()
                    snapshot.children.forEach { snapshot1 ->
                        val model =
                            snapshot1.getValue(com.example.whatsapp.Models.Message::class.java)

                        model?.let { message.add(it) }
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.send.setOnClickListener {
            val message1: String = binding.etMessage.text.toString()
            val model: com.example.whatsapp.Models.Message =
                com.example.whatsapp.Models.Message(senderId, message1)
            model.timestamp = System.currentTimeMillis()
            binding.etMessage.setText("")

            database.reference.child("chats").child(senderRoom).push().setValue(model)
                .addOnSuccessListener {
                    database.reference.child("chats").child(receiverRoom).push().setValue(model)
                        .addOnSuccessListener {

                        }
                }
        }
    }
}