package com.contactrapide.app.feature.onboarding

import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrButton
import com.contactrapide.app.core.design.component.CrButtonStyle
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import kotlinx.coroutines.launch

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String
)

private val pages = listOf(
    OnboardingPage(
        emoji = "🔍",
        title = "On sélectionne",
        description = "Un personnel rigoureusement sélectionné selon vos besoins : ménage, cuisine, garde d'enfants, chauffeur."
    ),
    OnboardingPage(
        emoji = "✅",
        title = "On vérifie",
        description = "Chaque candidat est vérifié : identité, références, expérience. Zéro mauvaise surprise."
    ),
    OnboardingPage(
        emoji = "🏠",
        title = "On place",
        description = "En 24 à 48h, le bon profil est chez vous. La tranquillité d'esprit n'a pas de prix."
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
    ) {
        // Bouton "Passer"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Passer",
                color = TextGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .padding(horizontal = 8.dp)
            )
        }

        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPageContent(pages[page])
        }

        // Indicateurs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (pagerState.currentPage == index) 24.dp else 8.dp, 8.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == index) Navy else TextGray.copy(alpha = 0.4f))
                )
            }
        }

        // Bouton
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            CrButton(
                text = if (pagerState.currentPage == pages.size - 1) "Commencer" else "Suivant",
                onClick = {
                    if (pagerState.currentPage == pages.size - 1) {
                        onFinished()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                style = CrButtonStyle.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Text(page.emoji, fontSize = 80.sp)
        }

        Spacer(Modifier.height(48.dp))

        Text(
            text = page.title,
            color = Navy,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = page.description,
            color = TextGray,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))
    }
}
