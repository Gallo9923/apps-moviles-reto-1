package com.example.instagram

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instagram.databinding.ActivityMainBinding
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.logInBtn.setOnClickListener {
            Log.e(">>>","Intent started")
            val intent = Intent(this, NavigatorActivity::class.java);
            startActivity(intent);
        }

        requestPermissions(arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ), 1)

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