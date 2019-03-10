package com.example.asus.spo

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        find.setOnClickListener {
            if(editText.text.toString() == "" ||
                    editText.text.toString().toDouble() < 3 ||
                        editText.text.toString().toDouble() > 5)

                Snackbar.make(ll, "Данные некоректны!", Snackbar.LENGTH_SHORT).show()

            else {
                val score = editText.text.toString().toDouble()
                val categoryIntent = Intent(this,Category::class.java)
                var isPaid = false
                var findInRegion = false
                if(checkPaid.isChecked) isPaid = true
                if(checkRegion.isChecked) findInRegion = true
                categoryIntent.putExtra("isPaid",isPaid)
                categoryIntent.putExtra("findInRegion",findInRegion)
                categoryIntent.putExtra("score",score)
                startActivity(categoryIntent)
            }
        }
    }
}
