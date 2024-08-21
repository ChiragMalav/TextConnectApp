package com.example.whatsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp.Models.Users
import com.example.whatsapp.databinding.ActivitySettingsBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var storage : FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        storage = FirebaseStorage.getInstance()
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()

        binding.backArrow.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        database.reference.child("Users").child(FirebaseAuth.getInstance().uid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(Users::class.java)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        binding.plus.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent , 33 )
        }
    }

    override protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data?.data != null){
            val sFile : Uri = data.data!!
            binding.profileImage.setImageURI(sFile)

            val reference :StorageReference = storage.getReference().child("profile_pictures").child(FirebaseAuth.getInstance().uid!!)

            reference.putFile(sFile).addOnSuccessListener { taskSnapshot ->
                // onSuccess code here
                reference.downloadUrl.addOnSuccessListener{
                    uri ->
                    database.getReference().child("Users").child(FirebaseAuth.getInstance().uid!!)
                        .child("profilepic").setValue(uri.toString())
                    Toast.makeText(this,"Profle pic updated",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

