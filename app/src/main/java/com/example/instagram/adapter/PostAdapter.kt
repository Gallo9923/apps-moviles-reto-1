package com.example.instagram.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.model.Post
import com.example.instagram.state.SharedPref
import java.io.File
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
        holder.caption.text = post.caption
        holder.location.text = post.location

        val format = SimpleDateFormat("d-MM-yy 'at' HH:mm")
        val dateString = format.format(post.date)
        holder.date.text = dateString

        val file = File(post.url)

        val bitmap = BitmapFactory.decodeFile(file?.path)
        holder.postImage.setImageBitmap(bitmap)

        val user = SharedPref.findUserById(post.userId!!)
        if (user != null){
            // Username
            holder.username.text = user.username

            // Profile Picture
            val url = user.profilePhotoURL
            if (!url.equals("")){
                val bitmap = BitmapFactory.decodeFile(url)
                holder.profileImage.foreground = BitmapDrawable(Resources.getSystem(), bitmap);
                //holder.profileImage.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return post.size
    }


}