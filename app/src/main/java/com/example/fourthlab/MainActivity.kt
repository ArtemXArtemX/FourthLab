package com.example.fourthlab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
const val EXTRA_ANSWER_SHOWN = "com.example.fourthlab.answer_shown"

data class Question(val textResId: Int, val answer: Boolean)

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var hintTextView: TextView
    private lateinit var answerTextView: TextView

    private var correctAnswersCount = 0
    private lateinit var cheatActivityLauncher: ActivityResultLauncher<Intent>

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

        // Инициализация элементов
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        hintTextView = findViewById(R.id.hint_text_view)
        answerTextView = findViewById(R.id.answer_text_view)

        cheatActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
        }

        updateQuestion()
        updateHintText()

        trueButton.setOnClickListener {
            checkAnswer(true)
            disableAnswerButtons()
            if (quizViewModel.currentIndex == questionBank.size - 1) {
                showResult()
                nextButton.visibility = View.INVISIBLE
            }
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            disableAnswerButtons()
            if (quizViewModel.currentIndex == questionBank.size - 1) {
                showResult()
                nextButton.visibility = View.INVISIBLE
            }
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatActivityLauncher.launch(intent)
            useHint()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            enableAnswerButtons()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun updateHintText() {
        hintTextView.text = "Осталось подсказок: ${quizViewModel.remainingHints}"
        if (quizViewModel.remainingHints <= 0) {
            cheatButton.isEnabled = false
            cheatButton.alpha = 0.5f
        } else {
            cheatButton.isEnabled = true
            cheatButton.alpha = 1.0f
        }
    }

    private fun useHint() {
        if (quizViewModel.remainingHints > 0) {
            quizViewModel.remainingHints--
            updateHintText()

            correctAnswersCount = 0
            quizViewModel.isCheater = true

            val correctAnswer = quizViewModel.currentQuestionAnswer
            answerTextView.text = if (correctAnswer) {
                getString(R.string.true_button)
            } else {
                getString(R.string.false_button)
            }
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> {
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> {
                correctAnswersCount++
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
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
