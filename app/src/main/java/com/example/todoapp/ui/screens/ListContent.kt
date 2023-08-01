package com.example.todoapp.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.theme.taskItemBackgroundColor
import com.example.todoapp.ui.theme.taskItemContentColor
import com.example.todoapp.util.Actions
import com.example.todoapp.util.RequestState
import com.example.todoapp.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    searchAppBarState: SearchAppBarState,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    onSwipeToDelete: (ToDoTask, Actions) -> Unit,
    paddingValues: PaddingValues
) {
    if (searchAppBarState == SearchAppBarState.TRIGGERED) {
        if (searchedTasks is RequestState.Success) {
            HandleTask(
                paddingValues = paddingValues,
                task = searchedTasks.data,
                navigateToTaskScreen = navigateToTaskScreen,
                onSwipeToDelete = onSwipeToDelete
            )
        }
    } else {
        if (allTasks is RequestState.Success) {
            HandleTask(
                paddingValues = paddingValues,
                task = allTasks.data,
                navigateToTaskScreen = navigateToTaskScreen,
                onSwipeToDelete = onSwipeToDelete
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandleTask(
    paddingValues: PaddingValues,
    onSwipeToDelete: (ToDoTask, Actions) -> Unit,
    task: List<ToDoTask>,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (task.isEmpty()) {
        EmptyContent()
    } else {
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(
                items = task,
                key = { task ->
                    task.id
                }
            ) { task ->
                val dismissState = rememberDismissState()
                val degrees by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f)
                val dismissDirection = dismissState.dismissDirection
                val isDismiss = dismissState.isDismissed(DismissDirection.EndToStart)
                if (isDismiss && dismissDirection == DismissDirection.EndToStart) {
                    val scope = rememberCoroutineScope()
                    scope.launch {
                        delay(300)
                        onSwipeToDelete(task, Actions.DELETE)
                    }
                }
                var itemAppeared by remember { mutableStateOf(false) }
                LaunchedEffect(key1 = true ){
                    itemAppeared = true
                }
                AnimatedVisibility(
                    visible = itemAppeared && !isDismiss,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                ) {
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            RedBackground(
                                degrees = degrees
                            )
                        }, dismissContent = {
                            Taskitem(toDoTask = task, navigateToTaskScreen = navigateToTaskScreen)
                        }
                    )


                }
            }
        }
    }


}

@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
            .padding(24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
fun Taskitem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.taskItemBackgroundColor,
        shape = RectangleShape,
        shadowElevation = 2.dp,
        onClick = { navigateToTaskScreen(toDoTask.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                ) {
                    Text(
                        text = toDoTask.title,
                        color = MaterialTheme.colorScheme.taskItemContentColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                Box(
                    modifier = Modifier.padding(12.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Canvas(
                        modifier = Modifier.size(16.dp)
                    ) {
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                text = toDoTask.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                color = MaterialTheme.colorScheme.taskItemContentColor,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun Pr() {
    Taskitem(toDoTask = ToDoTask(
        id = 0, title = "Title", description = "Description as long as", priority = Priority.LOW
    ), navigateToTaskScreen = {})
}
