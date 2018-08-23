package com.ignite.ignite

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainActivity : AppCompatActivity() {
    private val igniteDB = FirebaseDatabase.getInstance().getReference("question")
    private var questions = ArrayList<String>()
    private var last = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val askAgain = findViewById<Button>(R.id.newQuestion)

        askAgain.setOnClickListener {
            var rand : Int

            do {
                rand = Random().nextInt(questions.size)
            } while (rand == last)

            last = rand

            textView.text = questions[rand]
        }

        // Read from the database
        igniteDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (question in dataSnapshot.value as ArrayList<HashMap<String, String>>) {
                    questions.add(question["text"] as String)
                }

                var rand : Int

                do {
                    rand = Random().nextInt(questions.size)
                } while (rand == last)

                last = rand

                textView.text = questions[rand]
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }
}
