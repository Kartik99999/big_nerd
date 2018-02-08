package com.kartik.geoquiz

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kartik.geoquiz.Data.Question
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var questionBank = arrayOf(
            Question(textResourceID = R.string.question_australia, answerTrueFalse = true),
            Question(textResourceID = R.string.question_oceans, answerTrueFalse = true),
            Question(textResourceID = R.string.question_mideast, answerTrueFalse = false),
            Question(textResourceID = R.string.question_africa, answerTrueFalse = false),
            Question(textResourceID = R.string.question_americas, answerTrueFalse = true),
            Question(textResourceID = R.string.question_asia, answerTrueFalse = true),
            Question(R.string.question_president, true),
            Question(R.string.question_country, false),
            Question(R.string.question_mickey, false),
            Question(R.string.question_heptagon, false),
            Question(R.string.question_bats, true),
            Question(R.string.question_moon, false),
            Question(R.string.question_titanic, false),
            Question(R.string.question_lightning, true),
            Question(R.string.question_bollywood, false),
            Question(R.string.question_muscle, true)

    )
    private var currentIndex = 0
    private var score = 0
    private var isCheater = false
    private var isLastReached = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Creating ")
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            score = savedInstanceState.getInt(SCORE, 0)
            isLastReached = savedInstanceState.getBoolean(LAST_REACHED, false)
            val arrayListForAskedQuestions = arrayListOf<Boolean>()
            arrayListForAskedQuestions.addAll(savedInstanceState.getBooleanArray(ARRAY_DONE).toCollection(arrayListOf()))
            for (i in questionBank.indices) {
                questionBank[i].seen = arrayListForAskedQuestions[i]
            }
        }
        if (isLastReached) {
            showInvisibleButtons()
        }
        cheatButton.visibility = View.INVISIBLE
        questionTextView.setText(questionBank[currentIndex].textResourceID)
        questionTextView.setOnClickListener { nextQuestion() }
        nextButton.setOnClickListener {
            nextQuestion()
        }
        prevButton.setOnClickListener {
            prevQuestion()
        }
        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }
        updateScore()
        resetButton.setOnClickListener {
            currentIndex = 0
            score = 0
            for (i in questionBank.indices) {
                questionBank[i].seen = false
            }
            resetButton.visibility = View.INVISIBLE
            finalScoreTextView.visibility = View.INVISIBLE
            isLastReached = false
            isCheater = false
            questionTextView.setText(questionBank[currentIndex].textResourceID)
            updateScore()
        }
        cheatButton.setOnClickListener {
            var answerIsTrue: Boolean = questionBank[currentIndex].answerTrueFalse
            val intent = CheatActivity.newIntent(packageContext = applicationContext, isAnswerTrue = answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return
            }
            isCheater = CheatActivity.wasAnswerShown(data)
        }
    }

    private fun MainActivity.updateScore() {
        scoreTextView.text = "${getText(R.string.current_score)} $score of ${questionBank.size}"
    }

    private fun checkAnswer(check: Boolean) {
        if (questionBank[currentIndex].seen)
            return
        if (isCheater) {
            Toast.makeText(applicationContext, R.string.cheating, Toast.LENGTH_SHORT).show()
        } else {
            if (check == questionBank[currentIndex].answerTrueFalse) {
                Toast.makeText(applicationContext, R.string.correct_one, Toast.LENGTH_SHORT).show()
                score++
            } else {
                Toast.makeText(applicationContext, R.string.incorrect_one, Toast.LENGTH_SHORT).show()
            }
        }
        questionBank[currentIndex].seen = true
        updateScore()
        if (currentIndex == questionBank.size - 1) {
            isLastReached = true
            showInvisibleButtons()
        }
    }

    private fun MainActivity.showInvisibleButtons() {
        val finalScore = (score.toDouble() / questionBank.size) * 100.00
        Toast.makeText(applicationContext, "Your percentage score = $finalScore", Toast.LENGTH_LONG).show()
        finalScoreTextView.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE
        finalScoreTextView.text = "Your percentage score = $finalScore"

    }


    private fun prevQuestion() {
        updateScore()
        if (currentIndex == 0) {
            currentIndex = questionBank.size - 1
        } else
            currentIndex--
        questionTextView.setText(questionBank[currentIndex].textResourceID)
    }

    private fun nextQuestion() {
        isCheater = false
        updateScore()
        currentIndex = (currentIndex + 1) % questionBank.size
        questionTextView.setText(questionBank[currentIndex].textResourceID)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Start")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Destroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stop")
    }

    //saving question index while rotation
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState?.putInt(KEY_INDEX, currentIndex)
        outState?.putInt(SCORE, score)
        outState?.putBoolean(LAST_REACHED, isLastReached)
        val askedQuestions = ArrayList<Boolean>()
        questionBank.indices.mapTo(askedQuestions) { questionBank[it].seen }
//        for (i in questionBank.indices) {
//            askedQuestions.add(questionBank[i].seen)
//        }
        outState?.putBooleanArray(ARRAY_DONE, askedQuestions.toBooleanArray())
    }

    companion object {
        private val LAST_REACHED = "last is reached"
        private val TAG = "MainActivity GEO"
        private val KEY_INDEX = "index"
        private val SCORE = "current score"
        private val ARRAY_DONE = "is question asked"
        private val REQUEST_CODE_CHEAT = 0
    }
}
