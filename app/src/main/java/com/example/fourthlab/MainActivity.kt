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

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // Восстанавливаем состояние, если оно было сохранено
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(
                "currentIndex",
                0
            ) // Восстанавливаем индекс текущего вопроса
        }

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)

        updateQuestion()

        trueButton.setOnClickListener {
            checkAnswer(true)
            disableAnswerButtons()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            disableAnswerButtons()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            enableAnswerButtons()
        }

        prevButton.setOnClickListener {
            currentIndex = if (currentIndex == 0) {
                questionBank.size - 1
            } else {
                currentIndex - 1
            }
            updateQuestion()
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
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
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")
        outState.putInt("currentIndex", currentIndex)

        }
    }




