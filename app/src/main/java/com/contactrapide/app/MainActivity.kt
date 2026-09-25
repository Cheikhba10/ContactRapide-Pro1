package com.contactrapide.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.contactrapide.app.core.design.theme.ContactRapideTheme
import com.contactrapide.app.core.navigation.CrNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactRapideTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CrNavHost()
                }
            }
        }
    }
}
