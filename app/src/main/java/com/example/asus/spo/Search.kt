package com.example.asus.spo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

class Search : AppCompatActivity() {

    private val faculties = ArrayList<Faculty>()
    private val time:Long = 40
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private fun update() {

        runOnUiThread {

            viewManager = LinearLayoutManager(this)
            viewAdapter = MyAdapter(faculties)

            recyclerView = findViewById<RecyclerView>(R.id.list).apply {

                setHasFixedSize(true)

                layoutManager = viewManager

                adapter = viewAdapter

            }

            progress.visibility = View.INVISIBLE

        }


    }


    private fun updateWithPause(){
        val timer = Timer()

        progress.visibility = View.VISIBLE

        timer.schedule(kotlin.concurrent.timerTask {
            progress.progress++
            if (progress.progress >= time) {
                progress.progress = 0
                update()
                timer.cancel()
            }
        },0,time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Data")
        val myQuery = myRef.orderByChild("type")

        var isFirst = true

        myQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                faculties.add(dataSnapshot.getValue<Faculty>(Faculty::class.java)!!)

                if(isFirst){
                    isFirst = false
                    updateWithPause()
                }

                if(!faculties.last().live ||
                    faculties.last().score > intent.getDoubleExtra("score",3.0) ||
                    (faculties.last().region && !intent.getBooleanExtra("findInRegion",false)) ||
                    (faculties.last().price != 0 && !intent.getBooleanExtra("isPaid",false)) ||
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
