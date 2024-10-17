package com.example.fourthlab
import androidx.annotation.StringRes

data class QuizQuestion(val textResId: Int, val answer: Boolean)

private val questionBank = listOf(
    QuizQuestion(R.string.question_australia, true),
    QuizQuestion(R.string.question_oceans, true),
    QuizQuestion(R.string.question_mideast, false),
    QuizQuestion(R.string.question_africa, false),
    QuizQuestion(R.string.question_americas, true),
    QuizQuestion(R.string.question_asia, true)
)
