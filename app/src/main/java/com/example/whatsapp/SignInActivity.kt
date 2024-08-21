package com.example.whatsapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.whatsapp.Models.Users
import com.example.whatsapp.databinding.ActivitySignInBinding
import com.example.whatsapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        val database = Firebase.database.reference

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Login")
        mProgressDialog.setMessage("Logging in to your account")

        binding.btnSignIn.setOnClickListener {
            mProgressDialog.show()
            auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener(){
                task->
                if(task.isSuccessful){
                    mProgressDialog.dismiss()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    mProgressDialog.dismiss()
                    Toast.makeText(this,"Log-In Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tvClickSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        if(auth.currentUser!=null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.btnGoogle.setOnClickListener {
            val signInClient=googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    result->

    if(result.resultCode== Activity.RESULT_OK){
        val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)

        if(task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            val credential=GoogleAuthProvider.getCredential(account?.idToken,null)

            auth.signInWithCredential(credential).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"Signed-In with Google",Toast.LENGTH_LONG).show()
                    val users= Users(binding.etEmail.text.toString(),binding.etPassword.text.toString())
                    val id:String = it.getResult().getUser()!!.getUid()

                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    else{
        Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
    }
}
}