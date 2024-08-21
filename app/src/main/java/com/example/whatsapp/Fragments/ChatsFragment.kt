package com.example.whatsapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp.Adapters.UsersAdapter
import com.example.whatsapp.Models.Users
import com.example.whatsapp.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    private lateinit var list:ArrayList<Users>
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater, container, false)
        list=ArrayList()

        val adapter:UsersAdapter = UsersAdapter(list,getContext())

        binding.chatRecyclerView.adapter=adapter
        val layoutManager:LinearLayoutManager= LinearLayoutManager(context)
        binding.chatRecyclerView.layoutManager = layoutManager
        database=FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        database.reference.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                snapshot.children.forEach { dataSnapshot ->
                    val users=dataSnapshot.getValue(Users::class.java)
                    users?.userId = dataSnapshot.key
                    users?.let {
                        list.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return binding.root
    }
}