package com.contactrapide.app

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.contactrapide.app.core.design.theme.ContactRapideTheme
import com.contactrapide.app.core.navigation.CrNavHost
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init osmdroid
        try {
            Configuration.getInstance().load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            )
            Configuration.getInstance().userAgentValue = packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            ContactRapideTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CrNavHost()
                }
            }
        }
    }
}
