package com.example.instagram.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R

class PostView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profileImage: ImageView = itemView.findViewById(R.id.profileImage)
    var username: TextView = itemView.findViewById(R.id.username)
    var location: TextView = itemView.findViewById(R.id.location)
    var postImage: ImageView = itemView.findViewById(R.id.postImage)
    var caption: TextView = itemView.findViewById(R.id.caption)
}