package com.example.todoapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.ui.theme.topappBarBackgroundColor
import com.example.todoapp.ui.theme.topappBarContentColor
import com.example.todoapp.ui.viewmodels.SharedViewModel
import com.example.todoapp.util.Actions
import com.example.todoapp.util.SearchAppBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTask()
    }


    val allTask by sharedViewModel.allTask.collectAsState()
    val searchedTask by sharedViewModel.searchTask.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchtextState: String by sharedViewModel.searchTextState
    val action by sharedViewModel.action
    val scaffoldState = rememberBottomSheetScaffoldState()


    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabase(actions = action) },
        taskTitle = sharedViewModel.title.value,
        action = action,
        onUndoClicked = {
            sharedViewModel.action.value = it
        }
    )

    Scaffold(
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchtextState = searchtextState
            )
        },
        floatingActionButton = { ListFab(onFabClicked = navigateToTaskScreen) },
    ) { paddingValues ->
        ListContent(
            allTasks = allTask,
            navigateToTaskScreen = navigateToTaskScreen,
            paddingValues = paddingValues,
            searchAppBarState = searchAppBarState,
            searchedTasks = searchedTask,
            onSwipeToDelete = {toDoTask, actions ->
            sharedViewModel.action.value = actions
            sharedViewModel.updateTaskFields(selectedTask = toDoTask)

            }
        )
    }
}


@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchtextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {},
                onDeleteAllClicked = {
                    sharedViewModel.action.value = Actions.DELETE_ALL
                }
            )
        }else -> {
            SearchAppBar(
                text = searchtextState,
                onTextChange = { newText ->
                    sharedViewModel.searchTextState.value = newText
                               },
                onCloseClick = {
                    if (sharedViewModel.searchTextState.value == "") {
                        sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    } else {
                        sharedViewModel.searchTextState.value = ""
                    }
                               },
                onSearchClicked = {
                    sharedViewModel.searchDatabase(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Tasks", color = MaterialTheme.colorScheme.topappBarContentColor
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.topappBarBackgroundColor,
        ),
        actions = { ListAppBarActions(onSearchClicked, onSortClicked, onDeleteAllClicked) }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    SearchAction(onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAll(onDeleteAllClicked)
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), color = Color.Black
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.topappBarContentColor,
                    modifier = Modifier.alpha(0.7f)
                )
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.topappBarContentColor),
            singleLine = true,
            leadingIcon = {
                Icon(
                    modifier = Modifier.alpha(0.7f),
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.topappBarContentColor
                )
            },
            trailingIcon = {
                IconButton(onClick = { onCloseClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.topappBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked(text) }),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.topappBarContentColor,
                focusedContainerColor = MaterialTheme.colorScheme.topappBarBackgroundColor,
                unfocusedContainerColor = MaterialTheme.colorScheme.topappBarBackgroundColor
            )
        )
    }
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expandedState = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )
        DropdownMenu(
            expanded = expandedState,
            onDismissRequest = { expandedState = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = Priority.LOW.name, modifier = Modifier.padding(4.dp)
                    )
                },
                onClick = {
                    expandedState = false
                    onSortClicked(Priority.LOW)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "null",
                        tint = Priority.LOW.color
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = Priority.MEDIUM.name, modifier = Modifier.padding(4.dp)
                    )
                },
                onClick = {
                    expandedState = false
                    onSortClicked(Priority.MEDIUM)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "null",
                        tint = Priority.MEDIUM.color
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = Priority.HIGH.name,
                        modifier = Modifier.padding(4.dp)
                    )
                },
                onClick = {
                    expandedState = false
                    onSortClicked(Priority.HIGH)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "null",
                        tint = Priority.HIGH.color
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = Priority.NONE.name, modifier = Modifier.padding(4.dp)
                    )
                },
                onClick = {
                    expandedState = false
                    onSortClicked(Priority.NONE)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "null",
                        tint = Priority.NONE.color
                    )
                }
            )
        }
    }
}

@Composable
fun DeleteAll(
    onDeleteAllClicked: () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expandedState = true }
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.topappBarContentColor
        )
        DropdownMenu(expanded = expandedState,
            onDismissRequest = { expandedState = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Delete All", modifier = Modifier.padding(6.dp)
                    )
                },
                onClick = {
                    expandedState = false
                    onDeleteAllClicked()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                    )
                }
            )
        }
    }
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = { onFabClicked(-1) },
        shape = RoundedCornerShape(12.dp),
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color(0xFFE5CEFF)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySnackBar(
    scaffoldState: BottomSheetScaffoldState,
    handleDatabaseActions: () -> Unit,
    taskTitle: String,
    action: Actions,
    onUndoClicked: (Actions) -> Unit
) {
    handleDatabaseActions()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Actions.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name}: $taskTitle",
                    actionLabel = setActionLable(action)
                )
                undoDeletedTask(action = action, snackbarResult = snackBarResult, onUndoClicked = onUndoClicked)
            }
        }
    }
}

private fun setActionLable(action: Actions): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "ok"
    }
}

private fun undoDeletedTask(
    action: Actions,
    snackbarResult: SnackbarResult,
    onUndoClicked: (Actions) -> Unit
) {
    if (snackbarResult == SnackbarResult.ActionPerformed && action == Actions.DELETE) {
        onUndoClicked(Actions.UNDO)
    }
}