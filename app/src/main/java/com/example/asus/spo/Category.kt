package com.example.asus.spo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_category.*
import java.util.ArrayList
import java.util.HashMap

class Category : AppCompatActivity() {

    private val categories = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val searchIntent = Intent(this,Search::class.java)
        searchIntent.putExtra("isPaid",intent.getBooleanExtra("isPaid",true))
        searchIntent.putExtra("findInRegion",intent.getBooleanExtra("findInRegion",true))
        searchIntent.putExtra("score",intent.getDoubleExtra("score",3.0))




        categories.add("Медицина")
        categories.add("Ремонт и строительство")
        categories.add("Техник")
        categories.add("Социальные и экономические")

        val to = intArrayOf(R.id.category)
        val from = arrayOf("name")

        val data = ArrayList<HashMap<String, Any>>()
        for (i in categories) {
            val planet = HashMap<String, Any>()
            planet[from[0]] = i
            data.add(planet)
        }

        val adapter = SimpleAdapter(
            applicationContext,
            data, R.layout.category, from, to
        )

        list.adapter = adapter

        list.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            searchIntent.putExtra("category", categories[i])
            startActivity(searchIntent)
        }

    }
}
