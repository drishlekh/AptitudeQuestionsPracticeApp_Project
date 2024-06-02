package com.techmania.quizappproject

import android.content.Intent
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.techmania.quizappproject.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")
    var question = ""
    var answerA=""
    var answerB=""
    var answerC=""
    var answerD=""
    var correctAnswer = ""
    var questionCount =0
    var questionNumber = 1

    var userAnswer=""
    var userCorrect = 0
    var userWrong = 0

    lateinit var timer : CountDownTimer
    private val totalTime = 25000L //25 sec in millisec
    var timerContinue = false
    var leftTime = totalTime

    val auth =FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding=ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        //enableEdgeToEdge()
        setContentView(view)

        gameLogic()

        quizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }
        quizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()
        }
        quizBinding.textViewA.setOnClickListener {
            pauseTimer()
            userAnswer="a"
            if(correctAnswer==userAnswer){
                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
        quizBinding.textViewB.setOnClickListener {
            pauseTimer()
            userAnswer="b"
            if(correctAnswer==userAnswer){
                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
        quizBinding.textViewC.setOnClickListener {
            pauseTimer()
            userAnswer="c"
            if(correctAnswer==userAnswer){
                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
        quizBinding.textViewD.setOnClickListener {
            pauseTimer()
            userAnswer="d"
            if(correctAnswer==userAnswer){
                quizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                quizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }

    }

    private fun gameLogic(){

        restorOptions()

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount=snapshot.childrenCount.toInt()
                if(questionNumber<=questionCount) {

                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer =
                        snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD

                    quizBinding.progressBar3.visibility= View.INVISIBLE
                    quizBinding.linearLayout1.visibility=View.VISIBLE
                    quizBinding.linearLayout2.visibility=View.VISIBLE
                    quizBinding.linearLayout3.visibility=View.VISIBLE

                    startTimer()
                }
                else {
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("QuizAppProject!")
                    dialogMessage.setMessage("Congrats , You answered all the Questions. \n Wanna see the results ?")
                    dialogMessage.setCancelable(false)   //dialog message wont be closed if clicked outside the box
                    dialogMessage.setPositiveButton("see results") { dialogWindow, position  ->
                        sendScore()
                    }
                    //lambda reprsesentation
                    dialogMessage.setNegativeButton("Play Again"){ dialogWindow, position ->
                        val intent=Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()

                }
                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }
        })
}

    fun findAnswer(){
        when(correctAnswer){
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickable(){
        quizBinding.textViewA.isClickable=false
        quizBinding.textViewB.isClickable=false
        quizBinding.textViewC.isClickable=false
        quizBinding.textViewD.isClickable=false
    }

    fun restorOptions(){
        quizBinding.textViewA.setBackgroundColor(Color.BLUE)
        quizBinding.textViewB.setBackgroundColor(Color.BLUE)
        quizBinding.textViewC.setBackgroundColor(Color.BLUE)
        quizBinding.textViewD.setBackgroundColor(Color.BLUE)

        quizBinding.textViewA.isClickable=true
        quizBinding.textViewB.isClickable=true
        quizBinding.textViewC.isClickable=true
        quizBinding.textViewD.isClickable=true
    }

    private fun startTimer(){
        timer = object : CountDownTimer(leftTime,1000){
            override fun onTick(millisUntilFinish: Long) {
                leftTime=millisUntilFinish
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickable()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text="time is up ! try next question"
                timerContinue=false
            }

        }.start()
        timerContinue=true

    }

    fun updateCountDownText(){
        val remainingTime : Int = (leftTime/1000).toInt()
        quizBinding.textViewTime.text=remainingTime.toString()
    }
    fun pauseTimer(){
        timer.cancel()
        timerContinue=false
    }
    fun resetTimer(){
        pauseTimer()
        leftTime=totalTime
        updateCountDownText()
    }
    fun sendScore(){
        user?.let{
            val userUID=it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"scores sent to DB",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}