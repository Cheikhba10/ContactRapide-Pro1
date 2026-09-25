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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

private val services = listOf("Ménage", "Nounou", "Chauffeur", "Sécurité", "Polyvalent")
private val experiences = listOf("Débutant", "1-3 ans", "3-5 ans", "+5 ans")

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
            subtitle = "Rejoignez notre équipe",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Encart motivation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Green.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💼 Rejoignez ContactRapide", color = Green, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Vous cherchez un emploi stable à Dakar ? Envoyez-nous votre candidature. Nous vous contacterons rapidement.",
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

                    CrField(value = nom, onChange = { nom = it }, label = "Nom complet *")
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            CrField(value = age, onChange = { age = it }, label = "Âge", isNumber = true)
                        }
                        Box(modifier = Modifier.weight(2f)) {
                            CrField(value = telephone, onChange = { telephone = it }, label = "Téléphone *", isPhone = true)
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text("Service souhaité", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsRow(items = services, selected = service, onSelect = { service = it }, columns = 3)

                    Spacer(Modifier.height(14.dp))

                    Text("Expérience", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsRow(items = experiences, selected = experience, onSelect = { experience = it }, columns = 2)

                    Spacer(Modifier.height(14.dp))

                    CrField(value = quartier, onChange = { quartier = it }, label = "Quartier *")

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Présentez-vous (optionnel)") },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy,
                            unfocusedBorderColor = TextGray,
                            focusedLabelColor = Navy,
                            cursorColor = Navy
                        ),
                        maxLines = 4
                    )

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
                                erreur = "Nom et téléphone obligatoires"
                            } else if (service.isBlank()) {
                                erreur = "Choisissez un service"
                            } else if (quartier.isBlank()) {
                                erreur = "Indiquez votre quartier"
                            } else {
                                erreur = ""
                                val msg = buildString {
                                    appendLine("💼 CANDIDATURE — ContactRapide")
                                    appendLine()
                                    appendLine("👤 Nom : $nom")
                                    if (age.isNotBlank()) appendLine("🎂 Âge : $age ans")
                                    appendLine("📞 Téléphone : $telephone")
                                    appendLine("🛠️ Service : $service")
                                    if (experience.isNotBlank()) appendLine("⭐ Expérience : $experience")
                                    appendLine("📍 Quartier : $quartier")
                                    if (message.isNotBlank()) {
                                        appendLine()
                                        appendLine("📝 Présentation :")
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
                        "💡 Nous vous répondrons sous 48h.",
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
private fun CrField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    isPhone: Boolean = false,
    isNumber: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Navy,
            unfocusedBorderColor = TextGray,
            focusedLabelColor = Navy,
            cursorColor = Navy
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = when {
                isPhone -> KeyboardType.Phone
                isNumber -> KeyboardType.Number
                else -> KeyboardType.Text
            }
        )
    )
}

@Composable
private fun ChipsRow(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    columns: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(columns).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowItems.forEach { item ->
                    val isSelected = item == selected
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Navy else Navy.copy(alpha = 0.08f))
                            .clickable { onSelect(item) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            color = if (isSelected) White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
