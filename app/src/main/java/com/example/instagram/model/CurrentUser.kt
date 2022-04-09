package com.example.instagram.model

object CurrentUser {

    var user: User? = null

    fun logOut(){
        user = null
    }

    fun logIn(user: User){
        this.user = user
    }

}