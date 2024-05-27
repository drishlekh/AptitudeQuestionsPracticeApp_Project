package com.techmania.quizappproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.techmania.quizappproject.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgotBinding : ActivityForgotPasswordBinding
    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        forgotBinding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.buttonResetPass.setOnClickListener {
            val userEmail = forgotBinding.editTextEmail.text.toString()
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Password Link is sent",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}