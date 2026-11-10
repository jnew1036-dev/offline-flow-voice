package com.flow.offline

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pure programmatic UI (Zero XML requirement)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 80, 50, 50)
            setBackgroundColor(Color.parseColor("#F8F9FA"))
        }

        val title = TextView(this).apply {
            text = "Offline Voice Flow Setup"
            textSize = 22f
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 40)
        }
        layout.addView(title)

        // 1. Overlay Button
        val overlayBtn = Button(this).apply {
            text = "1. Allow Floating Mic Permission"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Overlay Permission Already Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        layout.addView(overlayBtn)

        // 2. Accessibility Button
        val accessibilityBtn = Button(this).apply {
            text = "2. Enable Accessibility Service"
            setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }
        layout.addView(accessibilityBtn)

        // 3. Audio Permission & Start Service
        val startBtn = Button(this).apply {
            text = "3. Start Floating Voice Typing"
            setOnClickListener {
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
                } else {
                    startFloatingService()
                }
            }
        }
        layout.addView(startBtn)

        setContentView(layout)
    }

    private fun startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Pehle Floating permission allow karein", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, FloatingMicService::class.java)
        startService(intent)
        Toast.makeText(this, "Floating Mic Screen Par Aa Gaya!", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startFloatingService()
        }
    }
}
