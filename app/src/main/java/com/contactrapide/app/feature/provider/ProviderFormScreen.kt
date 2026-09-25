package com.contactrapide.app.feature.provider

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrButton
import com.contactrapide.app.core.design.component.CrButtonStyle
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.IntentUtils

private val services = listOf("Menage", "Nounou", "Chauffeur", "Securite", "Polyvalent")
private val experiences = listOf("Debutant", "1-3 ans", "3-5 ans", "+5 ans")

@Composable
fun ProviderFormScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    var nom by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var quartier by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var erreur by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(
            title = "Devenir prestataire",
            subtitle = "Rejoignez notre equipe",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Green.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rejoignez ContactRapide", color = Green, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Vous cherchez un emploi stable a Dakar ? Envoyez-nous votre candidature. Nous vous contacterons rapidement.",
                        color = TextDark,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text("Votre profil", color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    SimpleF(value = nom, onChange = { nom = it }, label = "Nom complet")
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            SimpleF(value = age, onChange = { age = it }, label = "Age")
                        }
                        Box(modifier = Modifier.weight(2f)) {
                            SimpleF(value = telephone, onChange = { telephone = it }, label = "Telephone")
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text("Service souhaite", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsP(items = services, selected = service, onSelect = { service = it }, cols = 3)

                    Spacer(Modifier.height(14.dp))

                    Text("Experience", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsP(items = experiences, selected = experience, onSelect = { experience = it }, cols = 2)

                    Spacer(Modifier.height(14.dp))

                    SimpleF(value = quartier, onChange = { quartier = it }, label = "Quartier")

                    Spacer(Modifier.height(10.dp))

                    SimpleF(value = message, onChange = { message = it }, label = "Presentez-vous (optionnel)", multiLine = true)

                    if (erreur.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        Text(erreur, color = Color(0xFFC62828), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(20.dp))

                    CrButton(
                        text = "Envoyer ma candidature",
                        icon = "🚀",
                        onClick = {
                            if (nom.isBlank() || telephone.isBlank()) {
                                erreur = "Nom et telephone obligatoires"
                            } else if (service.isBlank()) {
                                erreur = "Choisissez un service"
                            } else if (quartier.isBlank()) {
                                erreur = "Indiquez votre quartier"
                            } else {
                                erreur = ""
                                val msg = buildString {
                                    appendLine("CANDIDATURE - ContactRapide")
                                    appendLine()
                                    appendLine("Nom : $nom")
                                    if (age.isNotBlank()) appendLine("Age : $age ans")
                                    appendLine("Telephone : $telephone")
                                    appendLine("Service : $service")
                                    if (experience.isNotBlank()) appendLine("Experience : $experience")
                                    appendLine("Quartier : $quartier")
                                    if (message.isNotBlank()) {
                                        appendLine()
                                        appendLine("Presentation :")
                                        appendLine(message)
                                    }
                                }
                                IntentUtils.whatsapp(context, msg)
                            }
                        },
                        style = CrButtonStyle.GREEN,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Nous vous repondrons sous 48h.",
                        color = TextGray,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SimpleF(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    multiLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = if (multiLine) Modifier.fillMaxWidth().height(110.dp) else Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = !multiLine,
        maxLines = if (multiLine) 4 else 1
    )
}

@Composable
private fun ChipsP(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    cols: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(cols).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowItems.forEach { item ->
                    val isSel = item == selected
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSel) Navy else Navy.copy(alpha = 0.08f))
                            .clickable { onSelect(item) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            color = if (isSel) White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
