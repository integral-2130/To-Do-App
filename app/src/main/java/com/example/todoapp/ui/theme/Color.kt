package com.example.todoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val LightGrey = Color(0xFFDBDBDB)
val MediumGrey = Color(0xFF8F8E8E)
val DarkGrey = Color(0xFF2C2C2C)




val highColor = Color(0xFFF5001C)
val mediumColor = Color(0xFFFFEB3B)
val lowColor = Color(0xFF24FF00)
val noneColor = Color(0xFFC7C7C7)

val ColorScheme.topappBarBackgroundColor:Color
@Composable
get() =  if (isSystemInDarkTheme()) Color.Black else Color(0xFFE5CEFF)

val ColorScheme.topappBarContentColor: Color
@Composable
get() =  if (isSystemInDarkTheme()) Color.White else Color.Black


val ColorScheme.taskItemBackgroundColor: Color
    @Composable
    get() =  if (isSystemInDarkTheme()) Color.Black else Color.White

val ColorScheme.taskItemContentColor: Color
    @Composable
    get() =  if (isSystemInDarkTheme()) Color.White else Color.Black