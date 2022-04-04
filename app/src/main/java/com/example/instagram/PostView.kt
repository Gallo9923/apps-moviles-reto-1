package com.example.instagram

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var profileImage: ImageView = itemView.findViewById(R.id.profileImage)
    var username: TextView = itemView.findViewById(R.id.username)
    var location: TextView = itemView.findViewById(R.id.location)
    var postImage: ImageView = itemView.findViewById(R.id.postImage)
    var caption: TextView = itemView.findViewById(R.id.caption)
}