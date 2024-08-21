package com.example.whatsapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsapp.Models.Users
import com.example.whatsapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        val database = Firebase.database
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Creating Account")
        mProgressDialog.setMessage("We're Creating your account")

        binding.btnSignUp.setOnClickListener{
            mProgressDialog.show()
            auth = FirebaseAuth.getInstance()
            val uid = auth.currentUser?.uid
            auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener(){

                task->
                if(task.isSuccessful){
                    mProgressDialog.dismiss();
                    Toast.makeText(this,"User Created Successfully",Toast.LENGTH_SHORT).show()
                    val users = Users(binding.etUsername.text.toString(),binding.etEmail.text.toString(),binding.etPassword.text.toString())
                    val id:String = task.getResult().getUser()!!.getUid()
                    database.reference.child("Users").child(id).setValue(users)
                }
                else{
                    mProgressDialog.dismiss();
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.tvAlreadyAccount.setOnClickListener {
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }
    }
}