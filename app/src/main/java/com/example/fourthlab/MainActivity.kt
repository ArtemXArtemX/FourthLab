package com.example.fourthlab

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private var correctAnswersCount = 0

    // Declare questionBank directly in MainActivity
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        // Restore currentIndex if there's saved state
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        updateQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
            disableAnswerButtons()
            handleLastQuestion()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            disableAnswerButtons()
            handleLastQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            enableAnswerButtons()
            if (quizViewModel.currentIndex == questionBank.size - 1) {
                nextButton.visibility = View.INVISIBLE
            }
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val intent = Intent(this, CheatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLastQuestion() {
        if (quizViewModel.currentIndex == questionBank.size - 1) {
            showResult()
            nextButton.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[quizViewModel.currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[quizViewModel.currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswersCount++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun disableAnswerButtons() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        trueButton.alpha = 0.5f
        falseButton.alpha = 0.5f
    }

    private fun enableAnswerButtons() {
        trueButton.isEnabled = true
        falseButton.isEnabled = true
        trueButton.alpha = 1.0f
        falseButton.alpha = 1.0f
    }

    private fun showResult() {
        val resultMessage = getString(R.string.result_message, correctAnswersCount, questionBank.size)
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show()
    }
}
