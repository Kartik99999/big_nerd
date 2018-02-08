package com.kartik.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cheat.*

class CheatActivity : AppCompatActivity() {

    private var answerTrue: Boolean = false    //will be changed in onCreate

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerButton.setOnClickListener {
            if (answerTrue)
                answerTextView.setText(R.string.true_button)
            else
                answerTextView.setText(R.string.false_button)
            setAnswerShownResult(true)
        }
    }

    companion object {
        val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
        val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

        fun newIntent(packageContext: Context, isAnswerTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue)
            return intent
        }

        fun wasAnswerShown(result: Intent) = result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)

    }
}
/*todo
GeoQuiz has a few major loopholes. For this challenge, you will busy yourself with closing them.
    Here are the loopholes in order, from easiest to hardest to close:

• Users can rotate CheatActivity after they cheat to clear out the cheating result.

• Once they get back from CheatActivity, users can rotate QuizActivity to clear out mIsCheater.

• Users can press NEXT until the question they cheated on comes back around.

*/