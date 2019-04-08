package com.example.wabprojectredo

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.wabprojectredo.classes.ChatMessage
import com.example.wabprojectredo.classes.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class SurveyActivity : AppCompatActivity() {

    companion object {

        var questionArray = emptyArray<Question>()

        /*used to get current month in listenForQuestions; first place is null so that
        1 corresponds with jan, 2 with feb, so on*/
        val months = arrayOf("null", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december")

        var questionNumber = 1

        var totalQuestions = 0

        var submittedYet = false

        var doneCollecting = false

        var isSomethingChecked = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        supportActionBar?.title = "Survey"

        resetVaiables()
        listenForQuestions()

        btn_survey_1.setOnClickListener {

            btn_survey_submitnext.setBackgroundColor(Color.BLUE)
            btn_survey_1.setBackgroundColor(Color.BLUE)
            isSomethingChecked = true
        }

        btn_survey_2.setOnClickListener {
            btn_survey_submitnext.setBackgroundColor(Color.BLUE)
            btn_survey_2.setBackgroundColor(Color.BLUE)
            isSomethingChecked = true
        }

        btn_survey_3.setOnClickListener {
            btn_survey_submitnext.setBackgroundColor(Color.BLUE)
            btn_survey_3.setBackgroundColor(Color.BLUE)
            isSomethingChecked = true
        }

        btn_survey_4.setOnClickListener {
            btn_survey_submitnext.setBackgroundColor(Color.BLUE)
            btn_survey_4.setBackgroundColor(Color.BLUE)
            isSomethingChecked = true
        }
        btn_survey_submitnext.setOnClickListener {

            //if the user has already selected their answer for current page & there are still questions remaining
            if (isSomethingChecked && questionNumber < questionArray.size) {
                questionNumber++
                setTotalQsAndSetUpPage()
            }

            //if all questions have been answered, finish the quiz
            else if (isSomethingChecked && questionNumber >= questionArray.size){
                val intent = Intent(this, FinishQuizActivity::class.java)
                startActivity(intent)
            }

            else if (!isSomethingChecked){
                Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show()
            }
        }
        //TODO: note that an array starts at 0, need to fix
    }

    private fun resetVaiables() {
        val emptyQuestionArray = emptyArray<Question>()
        questionArray = emptyQuestionArray

        questionNumber = 1
        totalQuestions = 0
        submittedYet = false
        doneCollecting = false
        isSomethingChecked = false
    }

    private fun setTotalQsAndSetUpPage() {
        totalQuestions = questionArray.size
        if ((questionNumber - 1) < questionArray.size){
            setUpPage()
            Log.d("QuizActivity", "Page set up")
        }
    }

    private fun setUpPage() {
        resetButtonColors()
        isSomethingChecked = false

        //set buttons to default colors and configurations - this is required when every new question shows up

        txtview_survey_questionnum.text = "Question $questionNumber)"
        txtview_survey_questionnum3.text = "$questionNumber / ${questionArray.size}"
        txtview_survey_qtext.text = questionArray[questionNumber - 1].actualQuestion

        btn_survey_1.text = questionArray[questionNumber - 1].option1
        btn_survey_2.text = questionArray[questionNumber - 1].option2
        btn_survey_3.text = questionArray[questionNumber - 1].option3
        btn_survey_4.text = questionArray[questionNumber - 1].option4
    }

    private fun highlightCorrectAnswer() {
        if (btn_survey_1.text == questionArray[questionNumber - 1].correctAnswer){
            btn_survey_1.setBackgroundColor(Color.GREEN)
        }
        else if (btn_survey_2.text == questionArray[questionNumber - 1].correctAnswer){
            btn_survey_2.setBackgroundColor(Color.GREEN)
        }
        else if (btn_survey_3.text == questionArray[questionNumber - 1].correctAnswer){
            btn_survey_3.setBackgroundColor(Color.GREEN)
        }
        else if (btn_survey_4.text == questionArray[questionNumber - 1].correctAnswer){
            btn_survey_4.setBackgroundColor(Color.GREEN)
        }
    }

    //this method is only called once
    fun listenForQuestions(){
        var questionArrayList = emptyList<Question>()
        var questionArrayListShuffled = emptyList<Question>()
        var questionArrayListFinal = emptyList<Question>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM")
        val formatted = current.format(formatter)
        val currentMonth = QuizActivity.months[formatted.toInt()]
        var count = 0
        var numberOfQuestions = 0
        val ref = FirebaseDatabase.getInstance().getReference("/survey")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val questionItem = p0.getValue(Question::class.java)

                //adding the individual questions to the array of questions
                if (questionItem != null) {
                    questionArray += questionItem
                    count++

                    //after all of the questions for this node are gathered, do this
                    if (count >= 10) {
                        doneCollecting = true

                        //convert the array of questions to a list, randomize them, get the first 5 and turn back into an array
                        //questionArrayList = questionArray.toMutableList()
                        //questionArrayListShuffled = questionArrayList.shuffled()
                        //questionArrayListFinal = questionArrayListShuffled.take(5)
                        //questionArray = questionArrayListFinal.toTypedArray()

                        setTotalQsAndSetUpPage()
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
        return
    }

    private fun resetButtonColors() {
        btn_survey_1.setBackgroundResource(android.R.drawable.btn_default)
        btn_survey_2.setBackgroundResource(android.R.drawable.btn_default)
        btn_survey_3.setBackgroundResource(android.R.drawable.btn_default)
        btn_survey_4.setBackgroundResource(android.R.drawable.btn_default)
        btn_survey_submitnext.setBackgroundResource(android.R.drawable.btn_default)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_report -> {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
