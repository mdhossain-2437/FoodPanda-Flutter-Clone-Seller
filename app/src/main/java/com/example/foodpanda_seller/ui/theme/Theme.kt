package com.example.foodpanda_seller.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = FoodPandaPrimary,
    onPrimary = Color.White,
    primaryContainer = FoodPandaPrimaryContainer,
    onPrimaryContainer = Color.White,
    secondary = FoodPandaSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color.Black,
    onSecondaryContainer = Color.White,
    background = FoodPandaBackground,
    onBackground = FoodPandaSecondary,
    surface = FoodPandaSurface,
    onSurface = FoodPandaSecondary,
    surfaceVariant = FoodPandaCardBackground,
    onSurfaceVariant = FoodPandaSecondaryText,
    error = FoodPandaError,
    onError = Color.White
)

@Composable
fun FoodPandaSellerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
