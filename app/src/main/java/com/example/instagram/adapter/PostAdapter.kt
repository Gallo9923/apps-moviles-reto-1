package com.example.instagram.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.GlideApp
import com.example.instagram.R
import com.example.instagram.model.Post
import com.example.instagram.model.User
import com.example.instagram.state.SharedPref
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.Format
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class PostAdapter : RecyclerView.Adapter<PostView>(){

    //private var post = ArrayList<Post>()
    private var post = SharedPref.posts
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

        val format = SimpleDateFormat("yyyy:MM:d 'at' HH:mm")
        val dateString = format.format(post.date)
        holder.date.text = dateString

        Log.e("POST URL", "url:${post.url}")
        val file = File(post.url)
        Log.e("PostAdapter", "${file?.path}")
        Log.e(">>>", "File: ${context.fileList().toString()}")
        val bitmap = BitmapFactory.decodeFile(file?.path)
        holder.postImage.setImageBitmap(bitmap)


//        val storage = FirebaseStorage.getInstance()
//        val gsPostReference = storage.getReferenceFromUrl("gs://apps-moviles-reto1.appspot.com/${post.url}")
//
//        GlideApp.with(this.context)
//            .load(gsPostReference)
//            .into(holder.postImage)

//        val query = Firebase.firestore.collection("users").whereEqualTo("username", post.username)
//        query.get().addOnCompleteListener { task ->
//            for (document in task.result) {
//                var user = document.toObject(User::class.java)
//                val gsProfileReference =
//                    storage.getReferenceFromUrl("gs://apps-moviles-reto1.appspot.com/${user.profilePhotoURL}")
//                GlideApp.with(this.context)
//                    .load(gsProfileReference)
//                    .into(holder.profileImage)
//            }
//        }

    }

    override fun getItemCount(): Int {
        return post.size
    }


}