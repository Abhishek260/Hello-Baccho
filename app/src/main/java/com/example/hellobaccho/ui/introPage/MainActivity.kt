package com.example.hellobaccho.ui.introPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hellobaccho.ui.introPage.ui.theme.HelloBacchoTheme
import com.example.hellobaccho.ui.rickAndMortyList.RickMortyListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloBacchoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var pageNo by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, end = 5.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Enter Page No")
                Text(
                    text = "*",
                    color = Color.Red, // Assuming danger color is red
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            TextField(
                value = pageNo,
                onValueChange = { pageNo = it },
                label = { Text("0") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                if (pageNo.isEmpty()) {
                    Toast.makeText(context, "Enter Page No", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, RickMortyListActivity::class.java)
                    intent.putExtra("pageNo", pageNo)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Submit")
        }
    }
}
