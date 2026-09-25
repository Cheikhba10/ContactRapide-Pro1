package com.contactrapide.app.feature.contact

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrButton
import com.contactrapide.app.core.design.component.CrButtonStyle
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.AppConstants
import com.contactrapide.app.core.utils.IntentUtils

@Composable
fun ContactScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(title = "Contact", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Carte numéro
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Navy),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📞", fontSize = 44.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        AppConstants.PHONE_DISPLAY,
                        color = White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Disponible 7j/7", color = Gold, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            CrButton(
                text = "Appeler maintenant",
                icon = "📞",
                onClick = { IntentUtils.call(context) },
                style = CrButtonStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )

            CrButton(
                text = "Ouvrir WhatsApp",
                icon = "💬",
                onClick = { IntentUtils.whatsapp(context) },
                style = CrButtonStyle.GREEN,
                modifier = Modifier.fillMaxWidth()
            )

            CrButton(
                text = "Copier le numéro",
                icon = "📋",
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText("ContactRapide", AppConstants.PHONE_DISPLAY)
                    )
                    Toast.makeText(context, "Numéro copié", Toast.LENGTH_SHORT).show()
                },
                style = CrButtonStyle.GOLD,
                modifier = Modifier.fillMaxWidth()
            )

            CrButton(
                text = "Partager l'application",
                icon = "📤",
                onClick = { IntentUtils.shareApp(context) },
                style = CrButtonStyle.GHOST,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Adresse : ${AppConstants.ADDRESS}",
                color = TextGray,
                fontSize = 13.sp
            )
        }
    }
}
