package com.contactrapide.app.feature.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White

private data class LegalDoc(
    val emoji: String,
    val title: String,
    val content: String
)

@Composable
fun LegalScreen(onBack: () -> Unit) {
    var selectedDoc by remember { mutableStateOf<LegalDoc?>(null) }

    val docs = listOf(
        LegalDoc("🔒", "Politique de confidentialite", LegalTexts.CONFIDENTIALITE),
        LegalDoc("📋", "Conditions generales d'utilisation", LegalTexts.CGU),
        LegalDoc("🏛️", "Mentions legales", LegalTexts.MENTIONS),
        LegalDoc("🍪", "Politique de cookies", LegalTexts.COOKIES),
        LegalDoc("💰", "Politique de remboursement", LegalTexts.REMBOURSEMENT)
    )

    if (selectedDoc != null) {
        DocumentDetail(
            doc = selectedDoc!!,
            onBack = { selectedDoc = null }
        )
    } else {
        Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
            CrTopBar(title = "Informations legales", onBack = onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Documents legaux de ContactRapide",
                    color = TextGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                docs.forEach { doc ->
                    DocCard(doc = doc, onClick = { selectedDoc = doc })
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DocCard(doc: LegalDoc, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Navy.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(doc.emoji, fontSize = 22.sp)
            }
            Spacer(Modifier.padding(horizontal = 6.dp))
            Text(
                text = doc.title,
                color = Navy,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text("›", color = TextGray, fontSize = 24.sp)
        }
    }
}

@Composable
private fun DocumentDetail(doc: LegalDoc, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(title = doc.title, onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Text(
                    text = doc.content,
                    color = TextDark,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(18.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
