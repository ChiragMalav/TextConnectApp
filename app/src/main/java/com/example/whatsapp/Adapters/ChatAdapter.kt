package com.example.whatsapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.Models.Message
import com.example.whatsapp.R
import com.google.firebase.auth.FirebaseAuth

public class ChatAdapter(private val message: ArrayList<Message>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    public class RecieverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val receiverMsg:TextView = itemView.findViewById(R.id.recieverText)
        val receiverTime:TextView= itemView.findViewById(R.id.recieverTime)
    }


    public class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val senderMsg:TextView = itemView.findViewById(R.id.senderText)
        val senderTime:TextView= itemView.findViewById(R.id.senderTime)
    }

    var SENDER_VIEW_TYPE : Int = 1
    var RECEIVER_VIEW_TYPE : Int = 2

    override fun getItemViewType(position: Int): Int {
        if(message.get(position).uId.equals(FirebaseAuth.getInstance().uid)){
            return SENDER_VIEW_TYPE
        }
        else{
            return RECEIVER_VIEW_TYPE
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == SENDER_VIEW_TYPE){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_sender,parent,false)
            return SenderViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_reciever,parent,false)
            return RecieverViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return message.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        val message : Message = message.get(position)

        if(holder is SenderViewHolder){
            holder.senderMsg.text = message.message
        }
        else if (holder is RecieverViewHolder){
            holder.receiverMsg.text = message.message
        }
    }
}