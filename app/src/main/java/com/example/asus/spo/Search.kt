package com.example.asus.spo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import java.util.Collections.sort

class Search : AppCompatActivity() {

    private val faculties = ArrayList<Faculty>()
    private val time:Long = 40
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    private fun update() {

        runOnUiThread {

            viewManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            viewAdapter = MyAdapter(faculties)

            recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.list).apply {

                setHasFixedSize(true)

                layoutManager = viewManager

                adapter = viewAdapter

            }

            progress.visibility = View.INVISIBLE

        }


    }


    private fun sortList(){
        faculties.sortWith(Comparator { p1, p2 ->
            when {
                p1.score.toDouble() > p2.score.toDouble() -> 1
                p1.score == p2.score -> 0
                else -> -1
            }
        })
    }

    private fun updateWithPause(){
        val timer = Timer()

        progress.visibility = View.VISIBLE

        timer.schedule(kotlin.concurrent.timerTask {
            progress.progress++
            if (progress.progress >= time) {
                progress.progress = 0
                sortList()
                update()
                timer.cancel()
            }
        },0,time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Лист1")
        val myQuery = myRef.orderByChild("type")

        var isFirst = true

        myQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                faculties.add(dataSnapshot.getValue<Faculty>(Faculty::class.java)!!)

                if(isFirst){
                    isFirst = false
                    updateWithPause()
                }

                if(faculties.last().live == "false" ||
                    faculties.last().score.toDouble() > intent.getDoubleExtra("score",3.0) ||
                    (faculties.last().region == "true" && !intent.getBooleanExtra("findInRegion",false)) ||
                    (faculties.last().price != "0" && !intent.getBooleanExtra("isPaid",false)) ||
                    faculties.last().category != intent.getStringExtra("category")
                )

                            faculties.removeAt(faculties.lastIndex)


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })



    }
}
