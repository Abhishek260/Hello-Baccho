package com.example.hellobaccho.ui.introPage.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import com.example.hellobaccho.ui.introPage.MainScreen

// Define your color scheme
private val LightColorScheme = lightColorScheme(
    primary = Blue500,
    onPrimary = Color.White,
    primaryContainer = Blue700,
    onPrimaryContainer = Color.White,
    secondary = Teal200,
    // Add other color attributes if needed
)

// Define Typography and Shapes for Material3
private val AppTypography = Typography()
private val AppShapes = Shapes()

@Composable
fun HelloBacchoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloBacchoTheme {
        MainScreen() // Ensure you have a MainScreen composable to preview
    }
}
