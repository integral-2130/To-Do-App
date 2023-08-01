package com.example.todoapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todoapp.components.priorityDropDown
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoTask
import com.example.todoapp.ui.theme.topappBarBackgroundColor
import com.example.todoapp.ui.theme.topappBarContentColor
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Actions


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    navigateToListScreen: (Actions) -> Unit,
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel
) {
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority
    val context = LocalContext.current
    Scaffold(
        topBar = {
            if (selectedTask == null) {
                NewTaskAppBar(
                    navigateToListScreen = { action ->
                        if (action == Actions.NO_ACTION) {
                            navigateToListScreen(action)
                        } else {
                            if (sharedViewModel.validiate()) {
                                navigateToListScreen(action)
                            } else {
                                displayToast(context)
                            }
                        }
                    }
                )
            } else {
                ExistingTaskAppBar(
                    selectedTask = selectedTask,
                    navigateToListScreen = { action ->
                        if (action == Actions.NO_ACTION) {
                            navigateToListScreen(action)
                        } else {
                            if (sharedViewModel.validiate()) {
                                navigateToListScreen(action)
                            } else {
                                displayToast(context)
                            }
                        }

                    }
                )
            }
        },
        content = {
            TaskContent(
                title = title,
                ontitleChange = { sharedViewModel.updateTitle(it) },
                description = description,
                onDescriptionChange = { sharedViewModel.description.value = it },
                priority = priority,
                onPrioritySelected = { sharedViewModel.priority.value = it }
            )
        })
}

fun displayToast(context: Context) {

    Toast.makeText(context, "Fields are Empty", Toast.LENGTH_SHORT).show()

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Actions) -> Unit
) {
    TopAppBar(
        navigationIcon = { BackAction(onBackClicked = navigateToListScreen) },
        title = {
            Text(
                text = "Add Task", color = MaterialTheme.colorScheme.topappBarContentColor
            )
        },
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.topappBarBackgroundColor)

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask, navigateToListScreen: (Actions) -> Unit
) {
    TopAppBar(
        navigationIcon = { CloseAction(onCloseClicked = navigateToListScreen) },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colorScheme.topappBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            DeleteAction(onDeleteClicked = navigateToListScreen)
            UpdateAction(onUpdateClicked = navigateToListScreen)

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.topappBarBackgroundColor)

    )
}

@Composable
fun CloseAction(onCloseClicked: (Actions) -> Unit) {

    IconButton(onClick = { onCloseClicked(Actions.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )


    }
}

@Composable
fun UpdateAction(onUpdateClicked: (Actions) -> Unit) {

    IconButton(onClick = { onUpdateClicked(Actions.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )


    }
}

@Composable
fun DeleteAction(onDeleteClicked: (Actions) -> Unit) {

    IconButton(onClick = { onDeleteClicked(Actions.DELETE) }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )


    }
}

@Composable
fun BackAction(onBackClicked: (Actions) -> Unit) {

    IconButton(onClick = { onBackClicked(Actions.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )


    }
}

@Composable
fun AddAction(onAddClicked: (Actions) -> Unit) {

    IconButton(onClick = { onAddClicked(Actions.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )


    }
}

@Composable
fun TaskContent(
    title: String,
    ontitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 70.dp, bottom = 8.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            value = title,
            onValueChange = { ontitleChange(it) },
            label = { Text(text = "Title") },
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
        priorityDropDown(priority = priority, onPrioritySelected = onPrioritySelected)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = "Description") },
            textStyle = MaterialTheme.typography.bodyMedium,
        )


    }

}

//@Preview
//@Composable
//fun pred() {
//    taskContent(
//        title = "",
//        ontitleChange = {},
//        description = "",
//        onDescriptionChange = {},
//        priority = Priority.HIGH,
//        onPrioritySelected = {}
//    )
//}