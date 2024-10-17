package com.example.fourthlab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.os.Build


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val apiLevelTextView: TextView = findViewById(R.id.api_level_text_view)
        val apiLevel = Build.VERSION.SDK_INT
        apiLevelTextView.text = "API Level: $apiLevel"

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        backButton = findViewById(R.id.back_button)

        showAnswerButton.setOnClickListener {
            val answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
            answerTextView.text = if (answerIsTrue) {
                getString(R.string.true_button)
            } else {
                getString(R.string.false_button)
            }
        }

        backButton.setOnClickListener {
            val data = Intent()
            data.putExtra(EXTRA_ANSWER_SHOWN, true)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    companion object {
        const val EXTRA_ANSWER_IS_TRUE = "com.example.fourthlab.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.example.fourthlab.answer_shown"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
