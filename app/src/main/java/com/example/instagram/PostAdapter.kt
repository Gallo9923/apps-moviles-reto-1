package com.example.instagram

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter : RecyclerView.Adapter<PostView>(){

    private val post = ArrayList<Post>()

    init {
        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostView {

        var inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.post_row, parent, false )
        val postView = PostView(row)
        return postView
    }

    override fun onBindViewHolder(holder: PostView, position: Int) {
        val post = post[position]
        holder.username.text = post.username
        holder.caption.text = post.caption
        holder.location.text = post.location
    }

    override fun getItemCount(): Int {
        return post.size
    }


}