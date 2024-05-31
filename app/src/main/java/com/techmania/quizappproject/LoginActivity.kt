package com.techmania.quizappproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.techmania.quizappproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding

    val auth = FirebaseAuth.getInstance()

    //lateinit var googleSignInClient : GoogleApiClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        enableEdgeToEdge()
        setContentView(view)

        val textOfGoogleButton = loginBinding.buttonGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize=18F

        loginBinding.buttonSignIn.setOnClickListener {
            val userEmail = loginBinding.editTextLoginEmail.text.toString()
            val  userPassword = loginBinding.editTextLoginPassword.text.toString()
            signInUser(userEmail,userPassword)
        }


//        loginBinding.buttonGoogleSignIn.setOnClickListener {
//
//        }


        loginBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        loginBinding.textViewForgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    fun signInUser(userEmail : String , userPassword : String){
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Welcome...",Toast.LENGTH_LONG).show()
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user!=null){
            Toast.makeText(applicationContext,"Welcome...",Toast.LENGTH_LONG).show()
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}










