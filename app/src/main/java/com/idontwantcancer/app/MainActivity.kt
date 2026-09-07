package com.idontwantcancer.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.idontwantcancer.app.presentation.app.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private var initialSignalId by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        initialSignalId = intent.getStringExtra("signalId")
        
        setContent {
            App(initialSignalId = initialSignalId)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Ensure deep links work even when the app is already in the background
        val signalId = intent.getStringExtra("signalId")
        if (signalId != null) {
            initialSignalId = signalId
        }
    }
}
