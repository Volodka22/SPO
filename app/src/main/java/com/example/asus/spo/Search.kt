package com.example.asus.spo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
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


    private fun update() {

        runOnUiThread {

            val to = intArrayOf(R.id.name, R.id.info,R.id.score)
            val from = arrayOf("number + name", "info","score")

            val data = ArrayList<HashMap<String, Any>>()
            for (i in faculties) {
                val faculty = HashMap<String, Any>()
                faculty[from[0]] = i.name
                if(i.price != 0)
                    faculty[from[1]] = i.address + "\n" + "платно: " + i.price + " рублей"
                else
                    faculty[from[1]] = i.address + "\n" + "Бюджет"
                faculty[from[2]] =  i.score
                data.add(faculty)
            }

            val adapter = SimpleAdapter(
                applicationContext,
                data, R.layout.faculty, from, to
            )

            list.adapter = adapter

            list.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                val profile = Intent(this,Profile::class.java)
                profile.putExtra("faculty",faculties[i])
                startActivity(profile)
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


     //   faculties.add(Faculty("Строительство и эксплуатация зданий и сооружений","",1,"",
     //       1,"г.Калининград ул.Можайская д.60  \n   Обучение: платное 170000000000000",true,true,true,true,true))


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Data")
        val myQuery = myRef.orderByChild("type")


        myQuery.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                faculties.add(dataSnapshot.getValue<Faculty>(Faculty::class.java)!!)

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



        updateWithPause()



    }
}
