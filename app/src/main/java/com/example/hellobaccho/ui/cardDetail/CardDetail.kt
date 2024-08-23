package com.example.hellobaccho.ui.cardDetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.hellobaccho.dataModels.Location
import com.example.hellobaccho.dataModels.Origin
import com.example.hellobaccho.ui.cardDetail.ui.theme.HelloBacchoTheme
import com.example.hellobaccho.dataModels.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class CardDetail : ComponentActivity() {
    private var cardData: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardData = getCardData()
        setContent {
            HelloBacchoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CardDetailContent(cardData)
                }
            }
        }
    }

    private fun getCardData(): Result? {
        val jsonString = intent?.getStringExtra("CardData")
        return if (!jsonString.isNullOrEmpty()) {
            val gson = Gson()
            val listType = object : TypeToken<Result>() {}.type
            gson.fromJson(jsonString, listType)
        } else {
            null
        }
    }

    open fun formatDateTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm:ss, dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }
}

@Composable
fun CardDetailContent(cardData: Result?) {
    val formattedDate = remember { cardData?.created?.let { CardDetail().formatDateTime(it) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        cardData?.let {
            // Image
            val painter = rememberImagePainter(data = it.image)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(16.dp))
            )

            // Content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    // Centered Id
                    Text(
                        text = "Id: ${it.id}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    // Other Details
                    DetailRow(label = "Name", value = it.name)
                    DetailRow(label = "Status", value = it.status)
                    DetailRow(label = "Species", value = it.species)
                    DetailRow(label = "Type", value = it.type)
                    DetailRow(label = "Gender", value = it.gender)
                    DetailRow(label = "Total Episode No", value = it.episode.size.toString())
                    DetailRow(label = "Created", value = formattedDate ?: "")
                    DetailRow(label = "Origin", value = it.origin.name)
                    DetailRow(label = "Location", value = it.location.name)
                }
            }
        }
    }
}



@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}



@Preview(showBackground = true)
@Composable
fun CardDetailPreview() {
    HelloBacchoTheme {
        CardDetailContent(
            cardData = Result(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = Origin(name = "Earth (C-137)", url = ""),
                location = Location(name = "Earth (Replacement Dimension)", url = ""),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf("S01E01", "S01E02"),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            )
        )
    }
}

