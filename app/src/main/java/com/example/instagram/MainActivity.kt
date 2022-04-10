package com.example.instagram

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.databinding.ActivityMainBinding
import com.example.instagram.state.SharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing SharedPref
        SharedPref.sharedPref = getPreferences(MODE_PRIVATE)
        SharedPref.loadUsers()
        SharedPref.loadPreviousUser()
        SharedPref.loadPosts()

        // Bindings
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.logInBtn.setOnClickListener {
            logIn()
        }

        requestPermissions(arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ), 1)

        logInPreviousUser()
    }

    private fun logInPreviousUser(){
        val currentUser = SharedPref.currentUser
        if(currentUser != null){
            openAppToUser()
        }
    }

    private fun openAppToUser(){
        val intent = Intent(this, NavigatorActivity::class.java);
        startActivity(intent);
    }


    override fun onRestart() {
        super.onRestart()
        binding.usernameInTxt.setText("")
        binding.passwordInText.setText("")
    }

    private fun logIn(){
        val username = binding.usernameInTxt.text.toString()
        val password = binding.passwordInText.text.toString()

        val user = SharedPref.findUserByUsername(username)
        if (user != null && user.password.equals(password)){
            SharedPref.logIn(user)
            openAppToUser()
        }else {
            showWrongCredentialsDialog()
        }
    }

    private fun showWrongCredentialsDialog(){
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.incorrect_login)
        dialog.show()
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
            if (!grantAllPermissions){
                Toast.makeText(this, "All permissions must be accepted", Toast.LENGTH_SHORT).show()
            }
        }

    }
}