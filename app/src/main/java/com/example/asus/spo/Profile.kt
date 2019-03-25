package com.example.asus.spo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*
import android.R.attr.dial
import android.content.Intent
import android.net.Uri


class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val faculty = intent.getSerializableExtra("faculty") as Faculty

        name.text = faculty.name
        var information = ""

        information += "Категория: " + faculty.category + "\n"
        information += "Номер телефона: " + faculty.number + "\n"
        information += "Срок обучения: " + faculty.continuance + "\n"
        information += "Минимальный балл в 2018: " + faculty.score + "\n"
        information += "Адрес: " + faculty.address + "\n"
        information += "Бюджетных мест: " + faculty.count + "\n"
        information += "Ссылка на сайт: " + faculty.link + "\n"
        information += if(faculty.price == "0") "Бюджет\n"
                else "Стоимость обучения: " + faculty.price + "\n"
        information += "Форма обучения: "
        information += when {
            faculty.intramural == "1" -> "очная\n"
            faculty.intramural == "2" -> "заочная\n"
            else -> "очно-заочная\n"
        }
        info.text = information

        link.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(faculty.link))
            startActivity(browserIntent)
        }

        call.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+faculty.number)))
        }

    }
}
