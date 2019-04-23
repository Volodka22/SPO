package com.example.asus.spo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class MyAdapter(private val facultys: ArrayList<Faculty>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    inner class MyViewHolder internal constructor(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        internal val nameView: TextView = view.findViewById(R.id.name)
        internal val infoView: TextView = view.findViewById(R.id.info)
        internal val scoreView: TextView = view.findViewById(R.id.score)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.faculty, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val faculty = facultys[position]
        holder.nameView.text = faculty.name
        if(faculty.price != "0")
            holder.infoView.text = faculty.address + "\n" + "платно: " + faculty.price + " рублей"
        else
            holder.infoView.text = faculty.address + "\n" + "Бюджет"

        holder.scoreView.text = "Минимальный балл в 2018: " + faculty.score.toString()

        holder.itemView.setOnClickListener{
            val profile = Intent(holder.itemView.context,Profile::class.java)
            profile.putExtra("faculty",faculty)
            holder.itemView.context.startActivity(profile)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = facultys.size
}