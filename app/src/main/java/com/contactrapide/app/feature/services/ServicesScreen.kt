package com.contactrapide.app.feature.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextGray

@Composable
fun ServicesScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(title = "Nos Services", onBack = onBack)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Écran Services (à venir)", color = TextGray, fontSize = 16.sp)
        }
    }
}
