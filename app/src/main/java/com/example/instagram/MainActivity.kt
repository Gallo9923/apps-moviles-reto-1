package com.example.instagram

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagram.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    val CURRENT_USER_STR: String = "currentUser"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.logInBtn.setOnClickListener {
//            val intent = Intent(this, NavigatorActivity::class.java);
//            startActivity(intent);
            logIn()
        }

        requestPermissions(arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ), 1)

        logInPreviousUser()
    }

    fun logInPreviousUser(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val json = sharedPref.getString(CURRENT_USER_STR, "NO_DATA")
        if (json != "NO_DATA"){
            var user = Gson().fromJson(json, User::class.java)
            CurrentUser.user = user

            val intent = Intent(this, NavigatorActivity::class.java);
            startActivity(intent);
        }
    }

    override fun onRestart() {
        super.onRestart()

        binding.usernameInTxt.setText("")
        binding.passwordInText.setText("")

    }

    private fun logIn(){

        val username = binding.usernameInTxt.text.toString()
        val password = binding.passwordInText.text.toString()

        val query = Firebase.firestore.collection("users").whereEqualTo("username", username)
        query.get().addOnCompleteListener { task ->
            var allowedLogIn = false
            var user: User? = null
            for(document in task.result){
                user = document.toObject(User::class.java)
                if (user.password.equals(password)){
                    allowedLogIn = true
                    break
                }
            }

            if (allowedLogIn){

                saveCurrentUser(user)

                val intent = Intent(this, NavigatorActivity::class.java);
                startActivity(intent);
            }
        }

    }

    private fun saveCurrentUser(user: User?){

        if (user == null)
            return

        val json = Gson().toJson(user)

        val sharedPref = getPreferences(MODE_PRIVATE)
        sharedPref.edit().putString(CURRENT_USER_STR, json).apply()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1){
            var grantAllPermissions = true
            for(result in grantResults){
                if(result == PackageManager.PERMISSION_DENIED){
                    grantAllPermissions = false
                }
            }

            if (grantAllPermissions){
                // TODO:
            }else{
                Toast.makeText(this, "All permissions must be accepted", Toast.LENGTH_SHORT).show()
            }
        }

    }
}