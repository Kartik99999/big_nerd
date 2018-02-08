package com.kartik.geoquiz.Data

/**
 * Created by Kartik Sethi on 01-01-2018.
 */
data class Question(val textResourceID:Int, val answerTrueFalse:Boolean, var seen:Boolean=false)