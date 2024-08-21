package com.example.whatsapp.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.ChatDetailActivity
import com.example.whatsapp.Models.Users
import com.example.whatsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class UsersAdapter(private val list: ArrayList<Users>, private val context: Context?) : RecyclerView.Adapter<UsersAdapter.ViewHolder>(){



    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView =itemView.findViewById(R.id.profile_image)
        val userName:TextView=itemView.findViewById(R.id.userName)
        val lastMessage:TextView=itemView.findViewById(R.id.lastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users:Users= list[position]

        FirebaseDatabase.getInstance().reference.child("chats")
            .child(FirebaseAuth.getInstance().uid + users.userId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChildren()){
                        snapshot.children.forEach { snapshot1 ->
                            holder.lastMessage.text = snapshot1.child("message").getValue().toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        Picasso.get().load(users.profilepic).placeholder(R.drawable.baseline_person_24).into(holder.image)
        holder.userName.setText(users.userName)

        holder.itemView.setOnClickListener {
            val intent=Intent(context,ChatDetailActivity::class.java)
            intent.putExtra("userId",users.userId)
            intent.putExtra("userName",users.userName)
            intent.putExtra("profilePic",users.profilepic)
            context?.startActivity(intent)
        }
        }
}