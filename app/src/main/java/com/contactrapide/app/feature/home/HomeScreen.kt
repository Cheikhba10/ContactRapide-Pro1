package com.contactrapide.app.feature.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.R
import com.contactrapide.app.core.design.component.CrActionCard
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.AppConstants
import com.contactrapide.app.core.utils.IntentUtils

private data class HomeCategory(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val color: Color
)

private val homeCategories = listOf(
    HomeCategory("🧹", "Ménage", "dès 5 000 F", Color(0xFF16A085)),
    HomeCategory("👶", "Nounou", "dès 6 000 F", Color(0xFFE67E22)),
    HomeCategory("🚗", "Chauffeur", "dès 10 000 F", Color(0xFF8E44AD)),
    HomeCategory("🤝", "Polyvalent", "dès 5 000 F", Color(0xFF2980B9))
)

@Composable
fun HomeScreen(
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onLocation: () -> Unit,
    onServices: () -> Unit,
    onAbout: () -> Unit,
    onContact: () -> Unit,
    onCategoryClick: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Navy, Color(0xFF0F2547))))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_contactrapide),
                contentDescription = "ContactRapide",
                modifier = Modifier.size(130.dp).clip(RoundedCornerShape(20.dp))
            )
            Spacer(Modifier.height(12.dp))
            Text("ContactRapide", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "Besoin d'une bonne à Dakar ?",
                color = Gold,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "On sélectionne. On vérifie. On place.",
                color = White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nos services", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(
                    "Details >",
                    color = Green,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onServices() }
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                homeCategories.forEachIndexed { index, cat ->
                    CategoryCircle(
                        category = cat,
                        onClick = { onCategoryClick(index) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Actions rapides", color = Navy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))

            CrActionCard(
                emoji = "📞",
                title = "Appeler",
                subtitle = AppConstants.PHONE_DISPLAY,
                backgroundColor = Navy,
                textColor = White,
                onClick = { IntentUtils.call(context); onCall() }
            )

            CrActionCard(
                emoji = "💬",
                title = "WhatsApp",
                subtitle = AppConstants.WHATSAPP_DISPLAY,
                backgroundColor = Green,
                textColor = White,
                onClick = { IntentUtils.whatsapp(context); onWhatsApp() }
            )

            CrActionCard(
                emoji = "📍",
                title = "Localisation",
                subtitle = "Parcelles rond-point / Agence Yas",
                backgroundColor = White,
                textColor = Navy,
                onClick = onLocation
            )

            CrActionCard(
                emoji = "ℹ️",
                title = "À propos",
                subtitle = "Informations & horaires",
                backgroundColor = White,
                textColor = Navy,
                onClick = onAbout
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            AppConstants.SLOGAN,
            color = Navy,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun CategoryCircle(category: HomeCategory, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp).clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(category.color, category.color.copy(alpha = 0.75f)))),
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, fontSize = 32.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            category.title,
            color = TextDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(Modifier.height(2.dp))
        Text(
            category.subtitle,
            color = TextGray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}
