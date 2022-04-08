package com.example.instagram.model

object CurrentUser {

    var user: User? = null

    fun logOut(){
        user = null
    }

}