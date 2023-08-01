package com.example.todoapp.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.todoapp.data.models.Priority
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun priorityDropDown(priority: Priority, onPrioritySelected: (Priority) -> Unit) {

    var expandedState by remember { mutableStateOf(false) }
    val angle:Float by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { expandedState = true }
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), shape = MaterialTheme.shapes.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .padding(4.dp)
        ) {
            drawCircle(color = priority.color)
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = priority.name,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(4.dp)
            )
        }
        IconButton(
            onClick = { expandedState = true }, modifier = Modifier
                .alpha(0.7f)
                .rotate(angle)
        ) {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(modifier = Modifier.fillMaxWidth(0.95f),expanded = expandedState, onDismissRequest = { expandedState = false }) {

            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Priority.LOW.color
                    )
                },
                text = { Text(text = Priority.LOW.name) },
                onClick = {
                    expandedState = false
                    onPrioritySelected(Priority.LOW)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Priority.NONE.color
                    )
                },
                text = { Text(text = Priority.NONE.name) },
                onClick = {
                    expandedState = false
                    onPrioritySelected(Priority.NONE)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Priority.MEDIUM.color
                    )
                },
                text = { Text(text = Priority.MEDIUM.name) },
                onClick = {
                    expandedState = false
                    onPrioritySelected(Priority.MEDIUM)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Priority.HIGH.color
                    )
                },
                text = { Text(text = Priority.HIGH.name) },
                onClick = {
                    expandedState = false
                    onPrioritySelected(Priority.HIGH)
                }
            )

        }

    }


}


@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Preview()
@Composable
fun PRIOD() {
    priorityDropDown(priority = Priority.LOW, onPrioritySelected = {})
}