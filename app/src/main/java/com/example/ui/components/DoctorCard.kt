package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Doctor
import com.example.ui.theme.MedicalAccentCoral
import com.example.ui.theme.MedicalDarkBlue
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalLightBlue
import com.example.ui.theme.MedicalPrimaryBlue
import com.example.ui.theme.MedicalTextPrimary
import com.example.ui.theme.MedicalTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DoctorCard(
    doctor: Doctor,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("doctor_card_${doctor.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Avatar, Name & Title, and Favorite Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Doctor Avatar Badge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MedicalLightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "طبيب",
                        tint = MedicalPrimaryBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = doctor.fullName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MedicalDarkBlue
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!doctor.title.isNullOrBlank()) {
                        Text(
                            text = doctor.title,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MedicalTextSecondary,
                                fontSize = 12.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Favorite Toggle Button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.testTag("fav_btn_${doctor.id}")
                ) {
                    Icon(
                        imageVector = if (doctor.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = if (doctor.isFavorite) "إزالة من المفضلة" else "إضافة للمفضلة",
                        tint = if (doctor.isFavorite) MedicalAccentCoral else MedicalTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: Specialty and Subspecialty
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (doctor.specialty.isNotBlank()) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = doctor.specialty,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MedicalGreen
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MedicalGreen.copy(alpha = 0.12f)
                        ),
                        border = null
                    )
                }

                if (!doctor.subSpecialty.isNullOrBlank()) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = doctor.subSpecialty,
                                fontSize = 11.sp,
                                color = MedicalPrimaryBlue
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MedicalPrimaryBlue.copy(alpha = 0.08f)
                        ),
                        border = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location & Clinic row
            val locationText = buildString {
                if (!doctor.governorate.isNullOrBlank()) append(doctor.governorate)
                if (!doctor.city.isNullOrBlank()) {
                    if (isNotEmpty()) append(" — ")
                    append(doctor.city)
                }
                if (!doctor.clinicName.isNullOrBlank()) {
                    if (isNotEmpty()) append(" • ")
                    append(doctor.clinicName)
                }
            }

            if (locationText.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "الموقع",
                        tint = MedicalTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MedicalTextSecondary,
                            fontSize = 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!doctor.workplace.isNullOrBlank() && doctor.clinicName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = "جهة العمل",
                        tint = MedicalTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = doctor.workplace,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MedicalTextSecondary,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row: Primary phone and Call button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (doctor.phonePrimary.isNotBlank()) {
                    Text(
                        text = doctor.phonePrimary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MedicalDarkBlue,
                            fontSize = 14.sp
                        )
                    )

                    FilledIconButton(
                        onClick = { dialPhone(context, doctor.phonePrimary) },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MedicalGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("call_btn_${doctor.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "اتصال",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Text(
                        text = "الهاتف غير متوفر",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MedicalTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

fun dialPhone(context: Context, phone: String) {
    if (phone.isBlank()) return
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "تعذر فتح تطبيق الاتصال", Toast.LENGTH_SHORT).show()
    }
}
