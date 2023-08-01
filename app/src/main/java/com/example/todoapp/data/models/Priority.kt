package com.example.todoapp.data.models

import androidx.compose.ui.graphics.Color
import com.example.todoapp.ui.theme.highColor
import com.example.todoapp.ui.theme.lowColor
import com.example.todoapp.ui.theme.mediumColor
import com.example.todoapp.ui.theme.noneColor

enum class Priority(val color: Color) {

    HIGH(highColor),
    MEDIUM(mediumColor),
    LOW(lowColor),
    NONE(noneColor)
}