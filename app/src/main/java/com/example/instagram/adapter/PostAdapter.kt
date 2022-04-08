package com.example.instagram.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.GlideApp
import com.example.instagram.R
import com.example.instagram.model.Post
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList

class PostAdapter : RecyclerView.Adapter<PostView>(){

    private var post = ArrayList<Post>()
    private lateinit var context: Context

//    init {
//        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
//        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
//        post.add(Post("Chris", Date(), "TODO", "This is a comment.", "The World"))
//    }

    fun setContext(context: Context){
        this.context = context
    }

    fun setPosts(posts: ArrayList<Post>){
        this.post = posts
        notifyDataSetChanged()
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

        // WORKING CODE!
        val storage = FirebaseStorage.getInstance()
        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference = storage.getReferenceFromUrl("gs://apps-moviles-reto1.appspot.com/${post.url}")

        GlideApp.with(this.context)
            .load(gsReference)
            .into(holder.postImage)
    }

    override fun getItemCount(): Int {
        return post.size
    }


}