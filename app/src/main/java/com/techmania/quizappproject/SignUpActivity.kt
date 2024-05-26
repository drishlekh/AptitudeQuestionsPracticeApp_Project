package com.techmania.quizappproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.techmania.quizappproject.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignUpBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signupBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signupBinding.root

        enableEdgeToEdge()
        setContentView(view)

        signupBinding.buttonSignUp.setOnClickListener {
            val email = signupBinding.editTextSignUpEmail.text.toString()
            val name = signupBinding.editTextName.text.toString()
            val password = signupBinding.editTextSignUpPassword.text.toString()

            signupWithFirebase(name, email, password)
        }
    }

    fun signupWithFirebase(name: String,email: String,password: String){
        signupBinding.progressBarSignup.visibility= View.VISIBLE
        signupBinding.buttonSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"account has been created",Toast.LENGTH_SHORT).show()
                finish()
                signupBinding.progressBarSignup.visibility=View.INVISIBLE
                signupBinding.buttonSignUp.isClickable=true
            }else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

}